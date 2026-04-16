package com.tvarah.service.impl;

import com.tvarah.exception.BadRequestException;
import com.tvarah.service.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private static final int OTP_EXPIRY_SECONDS = 300; // 5 minutes

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    private final ConcurrentHashMap<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

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

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
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

                          <!-- Header -->
                          <tr>
                            <td style="background-color:#1a1a2e;padding:36px 48px;text-align:center;">
                              <h1 style="color:#ffffff;margin:0;font-size:26px;letter-spacing:2px;font-weight:700;">TVARAH</h1>
                              <p style="color:#a0a8c0;margin:6px 0 0;font-size:13px;letter-spacing:1px;">Talent Intelligence &amp; Recruitment Operations</p>
                            </td>
                          </tr>

                          <!-- Body -->
                          <tr>
                            <td style="padding:40px 48px 24px;">
                              <h2 style="color:#1a1a2e;margin:0 0 12px;font-size:22px;">Your One-Time Password</h2>
                              <p style="color:#444444;line-height:1.7;margin:0 0 24px;">
                                Use the OTP below to proceed. This code is valid for <strong>5 minutes</strong> and can only be used once.
                              </p>
                            </td>
                          </tr>

                          <!-- OTP Box -->
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

                          <!-- Warning -->
                          <tr>
                            <td style="padding:0 48px 40px;">
                              <p style="color:#888888;font-size:13px;line-height:1.6;margin:16px 0 0;">
                                If you did not request this OTP, please ignore this email. Do not share this code with anyone.
                              </p>
                              <p style="color:#444444;font-size:14px;margin:24px 0 0;">Regards,<br><strong>Team Tvarah</strong></p>
                            </td>
                          </tr>

                          <!-- Footer -->
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
                """.formatted(otp, year);
    }

    private record OtpEntry(String otp, Instant expiry) {}
}
