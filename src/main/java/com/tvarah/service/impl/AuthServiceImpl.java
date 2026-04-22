package com.tvarah.service.impl;

import com.tvarah.exception.BadRequestException;
import com.tvarah.exception.UnauthorizedException;
import com.tvarah.model.response.AuthResponse;
import com.tvarah.model.response.TokenResponse;
import com.tvarah.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private static final int OTP_EXPIRY_SECONDS = 300;
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
  private static final int PASSWORD_LENGTH = 12;

  private final RestTemplate restTemplate;
  private final JavaMailSender mailSender;
  private final Keycloak keycloakAdmin;

  @Value("${keycloak.token-uri}")
  private String tokenUri;

  @Value("${keycloak.logout-uri}")
  private String logoutUri;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${app.mail.from}")
  private String fromEmail;

  @Value("${app.frontend.login-url}")
  private String loginUrl;

  private final ConcurrentHashMap<String, TokenResponse> tokenStore = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, OtpEntry> resetOtpStore = new ConcurrentHashMap<>();

  @Override
  public void login(String email, String password) {
    TokenResponse token = validateCredentials(email, password);
    tokenStore.put(email, token);
    log.info("Credentials validated, sending OTP to: {}", email);
    sendOtp(email);
  }

  @Override
  public AuthResponse verifyOtpAndGetToken(String email, String otp) {
    verifyOtp(email, otp);

    TokenResponse token = tokenStore.remove(email);
    if (token == null) {
      throw new BadRequestException("Session expired. Please login again.");
    }

    UserDetails userDetails = getUserDetails(email);
    log.info("OTP verified, returning token for: {} (firstTimeUser={})", email, userDetails.firstTimeUser());
    return new AuthResponse(token, userDetails.firstTimeUser(), userDetails.firstName(), userDetails.lastName());
  }

  @Override
  public void sendOtp(String email) {
    String otp = generateOtp();
    otpStore.put(email, new OtpEntry(otp, Instant.now().plusSeconds(OTP_EXPIRY_SECONDS)));
    log.info("Sending OTP to: {}", email);
    sendOtpEmail(email, otp);
  }

  @Override
  public void verifyOtp(String email, String otp) {
    OtpEntry entry = otpStore.get(email);

    if (entry == null) {
      throw new BadRequestException("No OTP found for this email. Please request a new one.");
    }
    if (Instant.now().isAfter(entry.expiry())) {
      otpStore.remove(email);
      throw new BadRequestException("OTP has expired. Please request a new one.");
    }
    if (!entry.otp().equals(otp)) {
      throw new BadRequestException("Invalid OTP. Please try again.");
    }

    otpStore.remove(email);
    log.info("OTP verified successfully for: {}", email);
  }

  @Override
  public void inviteUser(String email) {
    String password = generateRandomPassword();
    log.info("Creating Keycloak user for email: {}", email);
    createKeycloakUser(email, password);
    log.info("Sending invite email to: {}", email);
    sendInviteEmail(email, password);
  }

  @Override
  public void completeProfile(String keycloakUserId, String firstName, String lastName, String password) {
    try {
      UserRepresentation update = new UserRepresentation();
      update.setFirstName(firstName);
      update.setLastName(lastName);
      keycloakAdmin.realm(realm).users().get(keycloakUserId).update(update);

      CredentialRepresentation credential = new CredentialRepresentation();
      credential.setType(CredentialRepresentation.PASSWORD);
      credential.setValue(password);
      credential.setTemporary(false);
      keycloakAdmin.realm(realm).users().get(keycloakUserId).resetPassword(credential);

      log.info("Profile completed for Keycloak user: {}", keycloakUserId);
    } catch (Exception e) {
      log.error("Failed to complete profile for Keycloak user: {}", keycloakUserId, e);
      throw new BadRequestException("Failed to complete profile");
    }
  }

  @Override
  public void logout(String refreshToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("client_id", clientId);
    body.add("client_secret", clientSecret);
    body.add("refresh_token", refreshToken);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    try {
      restTemplate.postForEntity(logoutUri, request, Void.class);
      log.info("User session invalidated via Keycloak logout");
    } catch (HttpClientErrorException e) {
      log.warn("Keycloak logout returned client error: {}", e.getStatusCode());
      throw new BadRequestException("Invalid or already expired refresh token.");
    } catch (Exception e) {
      log.error("Keycloak logout call failed", e);
      throw new BadRequestException("Logout failed. Please try again.");
    }
  }

  @Override
  public void forgotPassword(String email) {
    List<UserRepresentation> users = keycloakAdmin.realm(realm).users().searchByEmail(email, true);
    if (users == null || users.isEmpty()) {
      throw new BadRequestException("No account found with this email address.");
    }
    String otp = generateOtp();
    resetOtpStore.put(email, new OtpEntry(otp, Instant.now().plusSeconds(OTP_EXPIRY_SECONDS)));
    log.info("Sending password reset OTP to: {}", email);
    sendResetPasswordOtpEmail(email, otp);
  }

  @Override
  public void resetPassword(String email, String otp, String newPassword) {
    OtpEntry entry = resetOtpStore.get(email);
    if (entry == null) {
      throw new BadRequestException("No OTP found for this email. Please request a new one.");
    }
    if (Instant.now().isAfter(entry.expiry())) {
      resetOtpStore.remove(email);
      throw new BadRequestException("OTP has expired. Please request a new one.");
    }
    if (!entry.otp().equals(otp)) {
      throw new BadRequestException("Invalid OTP. Please try again.");
    }
    resetOtpStore.remove(email);

    List<UserRepresentation> users = keycloakAdmin.realm(realm).users().searchByEmail(email, true);
    if (users == null || users.isEmpty()) {
      throw new BadRequestException("No account found with this email address.");
    }
    String keycloakUserId = users.get(0).getId();

    try {
      CredentialRepresentation credential = new CredentialRepresentation();
      credential.setType(CredentialRepresentation.PASSWORD);
      credential.setValue(newPassword);
      credential.setTemporary(false);
      keycloakAdmin.realm(realm).users().get(keycloakUserId).resetPassword(credential);
      log.info("Password reset successful for: {}", email);
    } catch (Exception e) {
      log.error("Failed to reset password for: {}", email, e);
      throw new BadRequestException("Failed to reset password. Please try again.");
    }
  }

  private void sendResetPasswordOtpEmail(String email, String otp) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom(fromEmail);
      helper.setTo(email);
      helper.setSubject("Confidential | Password Reset OTP for Tvarah");
      helper.setText(buildResetPasswordOtpEmailHtml(otp), true);
      mailSender.send(message);
      log.info("Password reset OTP email sent successfully to: {}", email);
    } catch (MessagingException e) {
      log.error("Failed to send password reset OTP email to: {}", email, e);
      throw new BadRequestException("Failed to send OTP email");
    }
  }

  private String buildResetPasswordOtpEmailHtml(String otp) {
    int year = java.time.Year.now().getValue();
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body style="margin:0;padding:0;background-color:#f0f2f5;font-family:Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f0f2f5;padding:40px 0;">
            <tr>
              <td align="center">
                <table width="700" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:10px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.10);">
                  <tr>
                    <td style="background-color:#1a1a2e;padding:36px 48px;text-align:center;">
                      <h1 style="color:#ffffff;margin:0;font-size:26px;letter-spacing:2px;font-weight:700;">TVARAH</h1>
                      <p style="color:#a0a8c0;margin:6px 0 0;font-size:13px;letter-spacing:1px;">Talent Intelligence &amp; Recruitment Operations</p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:40px 48px 24px;">
                      <h2 style="color:#1a1a2e;margin:0 0 12px;font-size:22px;">Password Reset Request</h2>
                      <p style="color:#444444;line-height:1.7;margin:0 0 24px;">
                        We received a request to reset your password. Use the OTP below to proceed.
                        This code is valid for <strong>5 minutes</strong> and can only be used once.
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:0 48px 24px;text-align:center;">
                      <table cellpadding="0" cellspacing="0" align="center"
                             style="background-color:#f6f8fc;border-radius:8px;border:1px solid #e2e8f0;">
                        <tr>
                          <td style="padding:24px 48px;text-align:center;">
                            <p style="margin:0 0 6px;color:#888888;font-size:12px;text-transform:uppercase;letter-spacing:1px;">One-Time Password</p>
                            <p style="margin:0;color:#1a1a2e;font-size:36px;font-weight:700;letter-spacing:10px;">%s</p>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:0 48px 40px;">
                      <p style="color:#888888;font-size:13px;line-height:1.6;margin:16px 0 0;">
                        If you did not request a password reset, please ignore this email. Your password will remain unchanged.
                      </p>
                      <p style="color:#444444;font-size:14px;margin:24px 0 0;">Regards,<br><strong>Team Tvarah</strong></p>
                    </td>
                  </tr>
                  <tr>
                    <td style="background-color:#f6f8fc;padding:20px 48px;text-align:center;border-top:1px solid #e8e8e8;">
                      <p style="color:#aaaaaa;font-size:12px;margin:0;">&copy; %d Tvarah. All rights reserved.</p>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """
        .formatted(otp, year);
  }

  private UserDetails getUserDetails(String email) {
    try {
      List<UserRepresentation> users = keycloakAdmin.realm(realm).users().searchByEmail(email, true);
      if (users == null || users.isEmpty()) {
        return new UserDetails(true, null, null);
      }
      UserRepresentation user = users.get(0);
      String firstName = user.getFirstName();
      String lastName = user.getLastName();
      boolean firstTimeUser = "PENDING".equals(firstName) || "PENDING".equals(lastName)
          || (firstName == null || firstName.isBlank()) && (lastName == null || lastName.isBlank());
      String resolvedFirst = (firstName == null || firstName.isBlank() || "PENDING".equals(firstName)) ? null : firstName;
      String resolvedLast = (lastName == null || lastName.isBlank() || "PENDING".equals(lastName)) ? null : lastName;
      return new UserDetails(firstTimeUser, resolvedFirst, resolvedLast);
    } catch (Exception e) {
      log.warn("Could not fetch user details for: {}", email, e);
      return new UserDetails(false, null, null);
    }
  }

  private record UserDetails(boolean firstTimeUser, String firstName, String lastName) {
  }

  private TokenResponse validateCredentials(String email, String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "password");
    body.add("client_id", clientId);
    body.add("client_secret", clientSecret);
    body.add("username", email);
    body.add("password", password);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    try {
      ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUri, request, TokenResponse.class);
      return response.getBody();
    } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.BadRequest e) {
      log.warn("Invalid credentials for: {}", email);
      throw new UnauthorizedException("Invalid credentials");
    } catch (Exception e) {
      log.error("Keycloak authentication error for: {}", email, e);
      throw new BadRequestException("Authentication service unavailable");
    }
  }

  private void createKeycloakUser(String email, String password) {
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(password);
    credential.setTemporary(false);

    UserRepresentation user = new UserRepresentation();
    user.setEmail(email);
    user.setUsername(email);
    user.setEnabled(true);
    user.setEmailVerified(true);
    user.setFirstName("PENDING");
    user.setLastName("PENDING");
    user.setRequiredActions(List.of());
    user.setCredentials(List.of(credential));

    try (Response response = keycloakAdmin.realm(realm).users().create(user)) {
      if (response.getStatus() == 409) {
        throw new BadRequestException("User with email " + email + " already exists in Keycloak");
      }
      if (response.getStatus() < 200 || response.getStatus() >= 300) {
        log.error("Keycloak user creation failed with status: {}", response.getStatus());
        throw new BadRequestException("Failed to create user in Keycloak");
      }
    }
  }

  private void sendOtpEmail(String email, String otp) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom(fromEmail);
      helper.setTo(email);
      helper.setSubject("Confidential | Login OTP for Tvarah");
      helper.setText(buildOtpEmailHtml(otp), true);
      mailSender.send(message);
      log.info("OTP email sent successfully to: {}", email);
    } catch (MessagingException e) {
      log.error("Failed to send OTP email to: {}", email, e);
      throw new BadRequestException("Failed to send OTP email");
    }
  }

  private void sendInviteEmail(String email, String password) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom(fromEmail);
      helper.setTo(email);
      helper.setSubject("Tvarah! Activate Your Access Today");
      helper.setText(buildInviteEmailHtml(email, password), true);
      mailSender.send(message);
      log.info("Invite email sent successfully to: {}", email);
    } catch (MessagingException e) {
      log.error("Failed to send invite email to: {}", email, e);
      throw new BadRequestException("Failed to send invite email");
    }
  }

  private String generateOtp() {
    SecureRandom random = new SecureRandom();
    return String.valueOf(100000 + random.nextInt(900000));
  }

  private String generateRandomPassword() {
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
    }
    return password.toString();
  }

  private String buildOtpEmailHtml(String otp) {
    int year = java.time.Year.now().getValue();
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body style="margin:0;padding:0;background-color:#f0f2f5;font-family:Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f0f2f5;padding:40px 0;">
            <tr>
              <td align="center">
                <table width="700" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:10px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.10);">
                  <tr>
                    <td style="background-color:#1a1a2e;padding:36px 48px;text-align:center;">
                      <h1 style="color:#ffffff;margin:0;font-size:26px;letter-spacing:2px;font-weight:700;">TVARAH</h1>
                      <p style="color:#a0a8c0;margin:6px 0 0;font-size:13px;letter-spacing:1px;">Talent Intelligence &amp; Recruitment Operations</p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:40px 48px 24px;">
                      <h2 style="color:#1a1a2e;margin:0 0 12px;font-size:22px;">Your One-Time Password</h2>
                      <p style="color:#444444;line-height:1.7;margin:0 0 24px;">
                        Use the OTP below to proceed. This code is valid for <strong>5 minutes</strong> and can only be used once.
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:0 48px 24px;text-align:center;">
                      <table cellpadding="0" cellspacing="0" align="center"
                             style="background-color:#f6f8fc;border-radius:8px;border:1px solid #e2e8f0;">
                        <tr>
                          <td style="padding:24px 48px;text-align:center;">
                            <p style="margin:0 0 6px;color:#888888;font-size:12px;text-transform:uppercase;letter-spacing:1px;">One-Time Password</p>
                            <p style="margin:0;color:#1a1a2e;font-size:36px;font-weight:700;letter-spacing:10px;">%s</p>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:0 48px 40px;">
                      <p style="color:#888888;font-size:13px;line-height:1.6;margin:16px 0 0;">
                        If you did not request this OTP, please ignore this email. Do not share this code with anyone.
                      </p>
                      <p style="color:#444444;font-size:14px;margin:24px 0 0;">Regards,<br><strong>Team Tvarah</strong></p>
                    </td>
                  </tr>
                  <tr>
                    <td style="background-color:#f6f8fc;padding:20px 48px;text-align:center;border-top:1px solid #e8e8e8;">
                      <p style="color:#aaaaaa;font-size:12px;margin:0;">&copy; %d Tvarah. All rights reserved.</p>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """
        .formatted(otp, year);
  }

  private String buildInviteEmailHtml(String email, String password) {
    int year = java.time.Year.now().getValue();
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body style="margin:0;padding:0;background-color:#f0f2f5;font-family:Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f0f2f5;padding:40px 0;">
            <tr>
              <td align="center">
                <table width="700" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:10px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.10);">
                  <tr>
                    <td style="background-color:#1a1a2e;padding:36px 48px;text-align:center;">
                      <h1 style="color:#ffffff;margin:0;font-size:26px;letter-spacing:2px;font-weight:700;">TVARAH</h1>
                      <p style="color:#a0a8c0;margin:6px 0 0;font-size:13px;letter-spacing:1px;">Talent Intelligence &amp; Recruitment Operations</p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:40px 48px 24px;">
                      <h2 style="color:#1a1a2e;margin:0 0 12px;font-size:22px;">Welcome to Tvarah Portal</h2>
                      <p style="color:#444444;line-height:1.7;margin:0 0 16px;">Hello,</p>
                      <p style="color:#444444;line-height:1.7;margin:0 0 16px;">
                        You have been invited to join <strong>Tvarah</strong> — a talent intelligence and recruitment operations platform.
                      </p>
                      <p style="color:#444444;line-height:1.7;margin:0 0 24px;">
                        Your account has been created. Use the credentials below to log in for the first time.
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:0 48px 24px;">
                      <table cellpadding="0" cellspacing="0" width="100%%"
                             style="background-color:#f6f8fc;border-radius:8px;border:1px solid #e2e8f0;">
                        <tr>
                          <td style="padding:14px 20px;border-bottom:1px solid #e2e8f0;">
                            <p style="margin:0 0 3px;color:#888888;font-size:11px;text-transform:uppercase;letter-spacing:0.5px;">Email</p>
                            <p style="margin:0;color:#1a1a2e;font-size:14px;font-weight:600;">%s</p>
                          </td>
                        </tr>
                        <tr>
                          <td style="padding:14px 20px;">
                            <p style="margin:0 0 3px;color:#888888;font-size:11px;text-transform:uppercase;letter-spacing:0.5px;">Password</p>
                            <p style="margin:0;color:#1a1a2e;font-size:14px;font-weight:700;letter-spacing:2px;">%s</p>
                          </td>
                        </tr>
                      </table>
                      <p style="color:#888888;font-size:12px;margin:10px 0 0;">You will be prompted to set a new password after your first login.</p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:8px 48px 36px;text-align:center;">
                      <a href="%s"
                         style="display:inline-block;background-color:#1a6ef5;color:#ffffff;text-decoration:none;padding:15px 44px;border-radius:6px;font-size:15px;font-weight:bold;letter-spacing:0.5px;">
                        Join Tvarah
                      </a>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding:0 48px 40px;">
                      <p style="color:#555555;line-height:1.7;font-size:14px;margin:0 0 8px;">
                        If you need help, contact <a href="mailto:support@tvarah.com" style="color:#1a1a2e;font-weight:600;">support@tvarah.com</a>.
                      </p>
                      <p style="color:#444444;font-size:14px;margin:16px 0 0;">Regards,<br><strong>Team Tvarah</strong></p>
                    </td>
                  </tr>
                  <tr>
                    <td style="background-color:#f6f8fc;padding:20px 48px;text-align:center;border-top:1px solid #e8e8e8;">
                      <p style="color:#aaaaaa;font-size:12px;margin:0;">
                        &copy; %d Tvarah. All rights reserved.<br>
                        <span style="font-size:11px;">If you were not expecting this invitation, please ignore this email.</span>
                      </p>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """
        .formatted(email, password, loginUrl, year);
  }

  private record OtpEntry(String otp, Instant expiry) {
  }
}
