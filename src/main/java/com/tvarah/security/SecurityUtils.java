package com.tvarah.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

/**
 * Utility methods for extracting authenticated user details from the security context.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<Jwt> getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return Optional.of(jwtToken.getToken());
        }
        return Optional.empty();
    }

    public static Optional<String> getCurrentUserId() {
        return getCurrentJwt().map(jwt -> jwt.getClaimAsString("sub"));
    }

    public static Optional<String> getCurrentUsername() {
        return getCurrentJwt().map(jwt -> jwt.getClaimAsString("preferred_username"));
    }

    public static Optional<String> getCurrentEmail() {
        return getCurrentJwt().map(jwt -> jwt.getClaimAsString("email"));
    }

    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }
}
