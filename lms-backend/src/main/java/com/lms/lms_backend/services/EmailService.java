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

    private String buildHtmlEmail(String title, String content) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                <h2 style="color: #0056b3; text-align: center;">Hệ Thống Lớp Học Ảo</h2>
                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
            
                <h3 style="color: #333;">%s</h3>
            
                <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px; color: #555; line-height: 1.6;">
                    %s
                </div>
            
                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="text-align: center; color: #888; font-size: 12px;">
                    Đây là email tự động, vui lòng không trả lời.<br>
                    &copy; 2026 Virtual Classroom Project.
                </p>
            </div>
            """.formatted(title, content.replace("\n", "<br>"));
    }
}