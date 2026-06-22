package com.nutritrack.dto.auth;

import jakarta.validation.constraints.NotBlank; // Import added
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token is required") // Added validation
    private String refreshToken;
}