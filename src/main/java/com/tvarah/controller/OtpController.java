package com.tvarah.controller;

import com.tvarah.model.request.OtpRequest;
import com.tvarah.model.request.OtpVerifyRequest;
import com.tvarah.model.response.ApiResponse;
import com.tvarah.service.OtpService;
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
@RequestMapping("/otp")
@RequiredArgsConstructor
@Tag(name = "OTP", description = "APIs for sending and verifying one-time passwords")
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    @Operation(
            summary = "Send OTP",
            description = "Generates a 6-digit OTP and sends it to the provided email address. Valid for 5 minutes."
    )
    public ResponseEntity<ApiResponse<Void>> sendOtp(@Valid @RequestBody OtpRequest request) {
        otpService.sendOtp(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("OTP sent successfully", null));
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify OTP",
            description = "Validates the OTP submitted for the given email. OTP is invalidated after successful verification."
    )
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        otpService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", null));
    }
}
