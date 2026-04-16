package com.tvarah.service;

import com.tvarah.model.response.TokenResponse;

public interface AuthService {

    void login(String email, String password);

    TokenResponse verifyOtpAndGetToken(String email, String otp);
}
