package com.tvarah.service;

import com.tvarah.model.response.AuthResponse;

public interface AuthService {

    void login(String email, String password);

    AuthResponse verifyOtpAndGetToken(String email, String otp);

    void sendOtp(String email);

    void verifyOtp(String email, String otp);

    void addUser(String firstName, String lastName, String email, String phoneNumber, String location, String department, String role);

    void resetPassword(String email);

    void logout(String refreshToken);

    AuthResponse refresh(String refreshToken);

    void changePassword(String email, String currentPassword, String newPassword);
}
