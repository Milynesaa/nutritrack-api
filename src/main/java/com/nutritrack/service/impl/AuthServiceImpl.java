package com.nutritrack.service.impl;

import com.nutritrack.config.JwtService;
import com.nutritrack.dto.auth.*;
import com.nutritrack.entity.PasswordResetToken;
import com.nutritrack.entity.RefreshToken;
import com.nutritrack.entity.User;
import com.nutritrack.entity.enums.Role;
import com.nutritrack.entity.enums.UserStatus;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.exception.UnauthorizedException;
import com.nutritrack.repository.PasswordResetTokenRepository;
import com.nutritrack.repository.RefreshTokenRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.AuthService;
import com.nutritrack.service.interfaces.EmailService;
import com.nutritrack.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.Optional;

@Slf4j
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

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Nutritionists start as PENDING, Patients as ACTIVE
        UserStatus status = request.getRole() == Role.ROLE_NUTRITIONIST
            ? UserStatus.PENDING
            : UserStatus.ACTIVE;

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                )
        );

        // Remove "Bearer " prefix if present
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        String refreshToken = createRefreshToken(user);

        log.info("User registered successfully: {}", user.getEmail());

        return AuthResponse.builder()
                .id(user.getId().toString())
                .token(cleanToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name().replace("ROLE_", ""))
                .build();
    }

    @Override
    @Transactional
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
                    return new ResourceNotFoundException("User not found");
                });

        // Check user status - prevent PENDING and REJECTED users from logging in
        if (user.getStatus() == UserStatus.PENDING) {
            log.warn("Pending nutritionist attempted login: {}", user.getEmail());
            throw new UnauthorizedException("Account pending approval. Please wait for administrator approval.");
        }

        if (user.getStatus() == UserStatus.REJECTED) {
            log.warn("Rejected nutritionist attempted login: {}", user.getEmail());
            throw new UnauthorizedException("Account rejected. Please contact support.");
        }

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                )
        );

        // Remove "Bearer " prefix if present
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        String refreshToken = createRefreshToken(user);

        log.info("User logged in successfully: {}", user.getEmail());

        return AuthResponse.builder()
                .id(user.getId().toString())
                .token(cleanToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name().replace("ROLE_", ""))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        log.info("Password reset request for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", request.getEmail());
                    return new ResourceNotFoundException("User not found");
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
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Password reset attempt with token");

        PasswordResetToken token = passwordResetTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> {
                    log.error("Invalid reset token");
                    return new UnauthorizedException("Invalid token");
                });

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.error("Reset token expired");
            throw new UnauthorizedException("Token expired");
        }

        User user = token.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        passwordResetTokenRepository.delete(token);

        log.info("Password reset successful for user: {}", user.getEmail());
    }

    @Transactional
    private String createRefreshToken(User user) {
        // Try to find existing refresh token for the user and update it to avoid duplicate key errors
        Optional<RefreshToken> existing = refreshTokenRepository.findByUser(user);
        if (existing.isPresent()) {
            RefreshToken refreshToken = existing.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(604800000L)); // 7 days
            refreshTokenRepository.save(refreshToken);
            return refreshToken.getToken();
        }

        // Otherwise, create a new one
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(604800000L)); // 7 days

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }
}