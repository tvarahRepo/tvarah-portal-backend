package com.tvarah.service.impl;

import com.tvarah.exception.BadRequestException;
import com.tvarah.service.InviteService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
  private static final int PASSWORD_LENGTH = 12;

  private final Keycloak keycloakAdmin;
  private final JavaMailSender mailSender;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${app.mail.from}")
  private String fromEmail;

  @Value("${app.frontend.login-url}")
  private String loginUrl;

  @Override
  public void inviteUser(String email) {
    String password = generateRandomPassword();

    log.info("Creating Keycloak user for email: {}", email);
    createKeycloakUser(email, password);

    log.info("Sending invite email to: {}", email);
    sendInviteEmail(email, password);
  }

  private void createKeycloakUser(String email, String password) {
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(password);
    credential.setTemporary(true);

    UserRepresentation user = new UserRepresentation();
    user.setEmail(email);
    user.setUsername(email);
    user.setEnabled(true);
    user.setEmailVerified(false);
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

  private void sendInviteEmail(String email, String password) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom(fromEmail);
      helper.setTo(email);
      helper.setSubject("Tvarah! Activate Your Access Today");
      helper.setText(buildEmailHtml(email, password), true);
      mailSender.send(message);
      log.info("Invite email sent successfully to: {}", email);
    } catch (MessagingException e) {
      log.error("Failed to send invite email to: {}", email, e);
      throw new BadRequestException("Failed to send invite email");
    }
  }

  private String generateRandomPassword() {
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
    }
    return password.toString();
  }

  private String buildEmailHtml(String email, String password) {
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

                  <!-- Header -->
                  <tr>
                    <td style="background-color:#1a1a2e;padding:36px 48px;text-align:center;">
                      <h1 style="color:#ffffff;margin:0;font-size:26px;letter-spacing:2px;font-weight:700;">TVARAH</h1>
                      <p style="color:#a0a8c0;margin:6px 0 0;font-size:13px;letter-spacing:1px;">Talent Intelligence &amp; Recruitment Operations</p>
                    </td>
                  </tr>

                  <!-- Welcome -->
                  <tr>
                    <td style="padding:40px 48px 24px;">
                      <h2 style="color:#1a1a2e;margin:0 0 12px;font-size:22px;">Welcome to Tvarah Portal</h2>
                      <p style="color:#444444;line-height:1.7;margin:0 0 16px;">Hello,</p>
                      <p style="color:#444444;line-height:1.7;margin:0 0 16px;">
                        You have been invited to join <strong>Tvarah</strong> — a talent intelligence and recruitment operations platform built to bring clarity, accountability, and rigor to hiring across every department and sector.
                      </p>
                      <p style="color:#444444;line-height:1.7;margin:0 0 24px;">
                        Your account has been created and is ready to use. Use the credentials below to log in for the first time.
                      </p>
                    </td>
                  </tr>

                  <!-- Credentials -->
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

                  <!-- CTA Button -->
                  <tr>
                    <td style="padding:8px 48px 36px;text-align:center;">
                      <a href="%s"
                         style="display:inline-block;background-color:#1a6ef5;color:#ffffff;text-decoration:none;padding:15px 44px;border-radius:6px;font-size:15px;font-weight:bold;letter-spacing:0.5px;">
                        Join Tvarah
                      </a>
                    </td>
                  </tr>

                  <!-- Support & Sign-off -->
                  <tr>
                    <td style="padding:0 48px 40px;">
                      <p style="color:#555555;line-height:1.7;font-size:14px;margin:0 0 8px;">
                        If you need any help accessing the portal, feel free to reach out to
                        <a href="mailto:support@tvarah.com" style="color:#1a1a2e;font-weight:600;">support@tvarah.com</a>
                        or your administrator.
                      </p>
                      <p style="color:#555555;line-height:1.7;font-size:14px;margin:0 0 24px;">Welcome to the Tvarah ecosystem!</p>
                      <p style="color:#444444;font-size:14px;margin:0;">Regards,<br><strong>Team Tvarah</strong></p>
                    </td>
                  </tr>

                  <!-- Footer -->
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
}
