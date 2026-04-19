package com.tvarah.controller;

import com.tvarah.model.request.CompleteProfileRequest;
import com.tvarah.model.request.InviteRequest;
import com.tvarah.model.request.LoginRequest;
import com.tvarah.model.request.OtpVerifyRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.model.response.AuthResponse;
import com.tvarah.security.SecurityUtils;
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

    @PostMapping("/invite-user")
    @Operation(summary = "Invite a user", description = "Creates a Keycloak account with a temporary password and sends an invitation email to the provided address.")
    public ResponseEntity<ApiResponse<Void>> invite(@Valid @RequestBody InviteRequest request) {
        authService.inviteUser(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Invitation sent successfully", null));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Validates email and password against Keycloak. On success, sends an OTP to the email for second-factor verification.")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
        authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("OTP sent to your email. Please verify to continue.", null));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP and get token", description = "Validates the OTP sent during login. Returns access token, refresh token, and a flag indicating whether this is the user's first login.")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        AuthResponse authResponse = authService.verifyOtpAndGetToken(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/register-user")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Register first-time profile", description = "Registers the first name and last name for a first-time user. Requires a valid bearer token obtained after OTP verification.")
    public ResponseEntity<ApiResponse<Void>> completeProfile(@Valid @RequestBody CompleteProfileRequest request) {
        String keycloakUserId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new com.tvarah.exception.UnauthorizedException("User not authenticated"));
        authService.completeProfile(keycloakUserId, request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", null));
    }
}
