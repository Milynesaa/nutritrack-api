package com.nutritrack.service.interfaces;

public interface EmailService {

    void sendPasswordResetEmail(String to, String token);
}
