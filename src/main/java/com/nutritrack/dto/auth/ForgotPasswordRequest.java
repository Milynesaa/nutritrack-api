package com.nutritrack.dto.auth;

import jakarta.validation.constraints.Email;     // Import added
import jakarta.validation.constraints.NotBlank; // Import added
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required") // Added validation
    @Email(message = "Invalid email format") // Added validation
    private String email;
}