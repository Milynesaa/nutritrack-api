package com.nutritrack.service.impl;

import com.nutritrack.service.interfaces.EmailService;
import com.nutritrack.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private static final org.slf4j.Logger log = LogUtil.getLogger(EmailServiceImpl.class);

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        log.info("Sending password reset email to: {}", to);

        String link = "http://localhost:8080/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperación de contraseña - NutriTrack");
        message.setText("Haz clic en este enlace para restablecer tu contraseña:\n" + link);

        try {
            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
