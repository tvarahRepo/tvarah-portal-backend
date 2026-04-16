package com.tvarah.service.impl;

import com.tvarah.exception.BadRequestException;
import com.tvarah.exception.UnauthorizedException;
import com.tvarah.model.response.TokenResponse;
import com.tvarah.service.AuthService;
import com.tvarah.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final OtpService otpService;
    private final RestTemplate restTemplate;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final ConcurrentHashMap<String, TokenResponse> tokenStore = new ConcurrentHashMap<>();

    @Override
    public void login(String email, String password) {
        TokenResponse token = validateCredentials(email, password);
        tokenStore.put(email, token);

        log.info("Credentials validated, sending OTP to: {}", email);
        otpService.sendOtp(email);
    }

    @Override
    public TokenResponse verifyOtpAndGetToken(String email, String otp) {
        otpService.verifyOtp(email, otp);

        TokenResponse token = tokenStore.remove(email);
        if (token == null) {
            throw new BadRequestException("Session expired. Please login again.");
        }

        log.info("OTP verified, returning token for: {}", email);
        return token;
    }

    private TokenResponse validateCredentials(String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", email);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUri, request, TokenResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.BadRequest e) {
            log.warn("Invalid credentials for: {}", email);
            throw new UnauthorizedException("Invalid credentials");
        } catch (Exception e) {
            log.error("Keycloak authentication error for: {}", email, e);
            throw new BadRequestException("Authentication service unavailable");
        }
    }
}
