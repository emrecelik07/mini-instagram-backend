package com.emrecelik.mini_instagram_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String email, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Welcome to Our Platform");
        message.setText("Welcome " + name + "!");
        mailSender.send(message);

    }

    public void sendResetOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Reset OTP");
        message.setText("Your one time password for resetting your password is:  " + otp);
        mailSender.send(message);
    }

    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Account Verification OTP");
        message.setText("Your one time password for verifying your account is: " + otp);
        mailSender.send(message);
    }
}
