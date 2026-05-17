package com.nutritrack.dto.auth;

import com.nutritrack.entity.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private Role role;
}