package com.nutritrack.service.interfaces;

import com.nutritrack.dto.auth.AuthResponse;
import com.nutritrack.dto.auth.ForgotPasswordRequest;
import com.nutritrack.dto.auth.LoginRequest;
import com.nutritrack.dto.auth.RegisterRequest;
import com.nutritrack.dto.auth.ResetPasswordRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
