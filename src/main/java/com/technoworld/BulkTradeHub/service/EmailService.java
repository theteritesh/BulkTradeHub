package com.technoworld.BulkTradeHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.User;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String token) {
        String name = user.getName();              
        String to = user.getEmail();              
        String subject = "Verify Your Email - Welcome to BulkTradeHub!";
        String verificationLink = "http://localhost:8080/verify?token=" + token;

        String body = "Hi " + name + ",\n\n"
            + "Thank you for registering with BulkTradeHub!\n"
            + "Please click the link below to verify your email address and activate your account:\n\n"
            + verificationLink + "\n\n"
            + "If you did not initiate this registration, you can safely ignore this email.\n\n"
            + "Best regards,\n"
            + "The BulkTradeHub Team !";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}

