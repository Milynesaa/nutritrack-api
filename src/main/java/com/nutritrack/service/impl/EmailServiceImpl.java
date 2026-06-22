package com.nutritrack.service.impl;

import com.nutritrack.exception.EmailServiceException;
import com.nutritrack.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        log.info("Sending password reset email to: {}", to);

        String link = resetPasswordUrl + "?token=" + token; // Use configurable URL

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperación de contraseña - NutriTrack");
        message.setText("Haz clic en este enlace para restablecer tu contraseña:\n" + link);

        try {
            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", to, e);
            throw new EmailServiceException("Failed to send password reset email to: " + to, e);
        }
    }
}