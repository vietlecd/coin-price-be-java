package com.javaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${email}")
    private String email;

    public void sendEmail(String to, String subject, String body) {
        try {
            System.out.println("Sending email to " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            System.out.println("Email: " + email);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            message.setText(body);
            message.setSubject(subject);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
