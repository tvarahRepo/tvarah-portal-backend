package com.tvarah.controller;

import com.tvarah.model.request.LoginRequest;
import com.tvarah.model.request.OtpVerifyRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.TokenResponse;
import com.tvarah.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Management", description = "APIs for authentication and token management")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Validates email and password against Keycloak. On success, sends an OTP to the email for second-factor verification."
    )
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
        authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("OTP sent to your email. Please verify to continue.", null));
    }

    @PostMapping("/verify-otp")
    @Operation(
            summary = "Verify OTP and get token",
            description = "Validates the OTP sent to the user's email. On success, returns the access token and refresh token."
    )
    public ResponseEntity<ApiResponse<TokenResponse>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        TokenResponse token = authService.verifyOtpAndGetToken(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("Login successful", token));
    }
}
