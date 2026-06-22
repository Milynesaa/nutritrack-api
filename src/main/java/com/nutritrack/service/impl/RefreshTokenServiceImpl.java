package com.nutritrack.service.impl;

import com.nutritrack.config.JwtService;
import com.nutritrack.dto.auth.AuthResponse;
import com.nutritrack.dto.auth.RefreshTokenRequest;
import com.nutritrack.entity.RefreshToken;
import com.nutritrack.entity.User;
import com.nutritrack.exception.UnauthorizedException;
import com.nutritrack.repository.RefreshTokenRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.RefreshTokenService;
import com.nutritrack.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Attempting to refresh token");
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> {
                    log.error("Invalid refresh token");
                    return new UnauthorizedException("Invalid refresh token");
                });

        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.error("Refresh token expired for user: {}", refreshToken.getUser().getEmail());
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token expired");
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
        // Invalidate existing refresh tokens for the user
        refreshTokenRepository.deleteByUser(user);
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(TokenGenerator.generateToken());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        
        refreshTokenRepository.save(refreshToken);
        
        log.info("New refresh token created for user: {} (previous tokens invalidated)", user.getEmail());
        return refreshToken.getToken();
    }
}
