package com.tvarah.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private TokenResponse token;
    private boolean firstTimeUser;
}
