package com.nutritrack.service.impl;

import com.nutritrack.config.JwtService;
import com.nutritrack.dto.auth.AuthResponse;
import com.nutritrack.dto.auth.RefreshTokenRequest;
import com.nutritrack.entity.RefreshToken;
import com.nutritrack.entity.User;
import com.nutritrack.repository.RefreshTokenRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.RefreshTokenService;
import com.nutritrack.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    private static final org.slf4j.Logger log = LogUtil.getLogger(RefreshTokenServiceImpl.class);

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Attempting to refresh token");
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> {
                    log.error("Invalid refresh token");
                    return new RuntimeException("Invalid refresh token");
                });

        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.error("Refresh token expired for user: {}", refreshToken.getUser().getEmail());
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();
        
        String newAccessToken = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                )
        );

        String newRefreshToken = createRefreshToken(user);

        log.info("Token refreshed successfully for user: {}", user.getEmail());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        log.info("Deleting refresh token");
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    private String createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        
        refreshTokenRepository.save(refreshToken);
        
        log.info("New refresh token created for user: {}", user.getEmail());
        return refreshToken.getToken();
    }
}
