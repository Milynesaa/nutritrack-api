package com.nutritrack.service.interfaces;

import com.nutritrack.dto.auth.RefreshTokenRequest;
import com.nutritrack.dto.auth.AuthResponse;

public interface RefreshTokenService {
    
    AuthResponse refreshToken(RefreshTokenRequest request);
    
    void deleteRefreshToken(String token);
}
