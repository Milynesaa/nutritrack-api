package com.nutritrack.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private String id;

    private String token;

    private String refreshToken;

    private String role;

    private String email;

    private String name;

    private String licenseNumber;

    private String message;
}