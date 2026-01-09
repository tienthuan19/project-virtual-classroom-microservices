package com.lms.lms_backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendAnnouncementEmail(String toEmail, String subject, String bodyContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            String htmlContent = buildHtmlEmail(subject, bodyContent);

            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }

    // Hàm tạo giao diện HTML cho email (Template GradingAI)
    private String buildHtmlEmail(String title, String content) {
        return """
            <div style="background-color: #f0f9ff; font-family: 'Segoe UI', Arial, sans-serif; padding: 40px 20px; margin: 0;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); overflow: hidden;">
  
                    <div style="height: 6px; background: linear-gradient(90deg, #06b6d4, #3b82f6, #8b5cf6); width: 100%%;"></div>

                    <div style="padding: 40px 32px;">
                        <div style="text-align: center; margin-bottom: 30px;">
                            <h1 style="margin: 0; font-size: 24px; font-weight: 800; color: #0f172a; letter-spacing: -0.5px;">
                                Grading<span style="color: #0ea5e9;">AI</span>
                            </h1>
                            <p style="margin: 5px 0 0 0; color: #64748b; font-size: 14px;">Hệ thống lớp học thông minh</p>
                        </div>

                        <h2 style="color: #1e293b; font-size: 20px; font-weight: 700; margin: 0 0 20px 0; line-height: 1.4;">
                            %s
                        </h2>

                        <div style="background-color: #f8fafc; border: 1px solid #e2e8f0; border-radius: 12px; padding: 24px; color: #334155; font-size: 16px; line-height: 1.6;">
                            %s
                        </div>

                        <div style="margin-top: 30px; text-align: center; border-top: 1px solid #f1f5f9; padding-top: 20px;">
                            <p style="color: #94a3b8; font-size: 13px; margin: 0; line-height: 1.5;">
                                Bạn nhận được email này vì bạn là thành viên của GradingAI.<br>
                                Đây là email tự động, vui lòng không trả lời.
                            </p>
                            <p style="color: #cbd5e1; font-size: 12px; margin-top: 10px;">
                                &copy; 2026 GradingAI. All rights reserved.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            """.formatted(title, content.replace("\n", "<br>"));
    }
}