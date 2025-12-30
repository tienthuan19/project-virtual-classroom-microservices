package com.lms.lms_backend.services;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage; // Nếu dùng mail thật
import org.springframework.mail.javamail.JavaMailSender; // Nếu dùng mail thật
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    // private final JavaMailSender javaMailSender;
    @Async
    public void sendAnnouncementEmail(String toEmail, String subject, String content) {

        System.out.println(">>> EMAIL SENT TO: " + toEmail);
        System.out.println("Subject: " + subject);
    }
}