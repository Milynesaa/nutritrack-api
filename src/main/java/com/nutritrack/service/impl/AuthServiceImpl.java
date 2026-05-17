package com.nutritrack.service.impl;

import com.nutritrack.config.JwtService;
import com.nutritrack.dto.auth.*;
import com.nutritrack.entity.PasswordResetToken;
import com.nutritrack.entity.RefreshToken;
import com.nutritrack.entity.User;
import com.nutritrack.repository.PasswordResetTokenRepository;
import com.nutritrack.repository.RefreshTokenRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.AuthService;
import com.nutritrack.service.interfaces.EmailService;
import com.nutritrack.util.LogUtil;
import com.nutritrack.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final org.slf4j.Logger log = LogUtil.getLogger(AuthServiceImpl.class);

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.List.of()
                )
        );

        String refreshToken = createRefreshToken(user);

        log.info("User registered successfully: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", request.getEmail());
                    return new RuntimeException("User not found");
                });

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.List.of()
                )
        );

        String refreshToken = createRefreshToken(user);

        log.info("User logged in successfully: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        log.info("Password reset request for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", request.getEmail());
                    return new RuntimeException("User not found");
                });

        String token = TokenGenerator.generateToken();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getEmail(), token);

        log.info("Password reset email sent to: {}", user.getEmail());

        return AuthResponse.builder()
                .message("Email sent successfully")
                .build();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Password reset attempt with token");

        PasswordResetToken token = passwordResetTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> {
                    log.error("Invalid reset token");
                    return new RuntimeException("Invalid token");
                });

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.error("Reset token expired");
            throw new RuntimeException("Token expired");
        }

        User user = token.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        passwordResetTokenRepository.delete(token);

        log.info("Password reset successful for user: {}", user.getEmail());
    }

    private String createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(604800000L)); // 7 days

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }
}
