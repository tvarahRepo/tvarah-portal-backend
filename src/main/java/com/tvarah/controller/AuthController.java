package com.tvarah.controller;

import com.tvarah.model.request.AddUserRequest;
import com.tvarah.model.request.ResetPasswordRequest;
import com.tvarah.model.request.LoginRequest;
import com.tvarah.model.request.LogoutRequest;
import com.tvarah.model.request.OtpVerifyRequest;
import com.tvarah.model.request.RefreshTokenRequest;
import com.tvarah.model.request.ChangePasswordRequest;
import com.tvarah.security.SecurityUtils;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.AuthResponse;
import com.tvarah.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "Auth Management", description = "APIs for authentication, OTP, and user invitation")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/add-user")
    @Operation(summary = "Add a user", description = "Creates a Keycloak account with the provided details and a temporary password, saves the user in the database with PENDING status, and sends an invitation email.")
    public ResponseEntity<ApiResponse<Void>> addUser(@Valid @RequestBody AddUserRequest request) {
        authService.addUser(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPhoneNumber(), request.getLocation(), request.getDepartment(), request.getRole());
        return ResponseEntity.ok(ApiResponse.success("User added successfully", null));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Validates email and password against Keycloak. On success, sends an OTP to the email for second-factor verification.")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
        authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("OTP sent to your email. Please verify to continue.", null));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP and get token", description = "Validates the OTP sent during login. Returns access token and refresh token.")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        AuthResponse authResponse = authService.verifyOtpAndGetToken(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Generates a new password for the user if they exist in the database with ACTIVE or PENDING status, and sends it to their email.")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("A new password has been sent to your email.", null));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Logout", description = "Invalidates the user's session in Keycloak by revoking the refresh token. Requires a valid bearer token.")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully.", null));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Uses a valid refresh token to obtain a new access token from Keycloak.")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", authResponse));
    }

    @PostMapping("/change-password")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Change password", description = "Validates the current password then updates it in Keycloak.")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        String email = SecurityUtils.getCurrentEmail()
                .orElseThrow(() -> new com.tvarah.exception.UnauthorizedException("User not authenticated"));
        authService.changePassword(email, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully.", null));
    }

}
