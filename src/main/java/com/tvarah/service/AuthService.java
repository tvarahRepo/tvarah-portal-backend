package com.tvarah.service;

import com.tvarah.model.response.AuthResponse;

public interface AuthService {

    void login(String email, String password);

    AuthResponse verifyOtpAndGetToken(String email, String otp);

    void sendOtp(String email);

    void verifyOtp(String email, String otp);

    void inviteUser(String email);

    void completeProfile(String keycloakUserId, String firstName, String lastName, String password);
}
