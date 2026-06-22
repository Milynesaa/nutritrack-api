package com.nutritrack.dto.auth;

import jakarta.validation.constraints.NotBlank; // Import added
import jakarta.validation.constraints.Size;    // Import added
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Token is required") // Added validation
    private String token;

    @NotBlank(message = "New password is required") // Added validation
    @Size(min = 6, message = "Password must be at least 6 characters") // Added validation
    private String newPassword;
}