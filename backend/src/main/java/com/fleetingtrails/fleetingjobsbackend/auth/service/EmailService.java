package com.fleetingtrails.fleetingjobsbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.backend-url}")
    private String backendUrl;

    public void sendEmailConfirmation(String toEmail, String token) {
        String confirmApiUrl = backendUrl + "/auth/confirm-email?token=" + token;
        String confirmFrontendUrl = frontendUrl + "/confirm-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Confirm your Fleeting Jobs account");
        message.setText(
                "Welcome to Fleeting Jobs.\n\n"
                        + "Confirm your email by opening this link:\n"
                        + confirmApiUrl
                        + "\n\n"
                        + "If you are using the web app, you can also use:\n"
                        + confirmFrontendUrl
                        + "\n\n"
                        + "This link expires in 24 hours.\n"
                        + "If you did not create an account, you can ignore this email."
        );

        mailSender.send(message);
    }
}
