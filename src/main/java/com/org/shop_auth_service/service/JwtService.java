package com.org.shop_auth_service.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateJwtToken(
            UserDetails userDetails
    );

    String extractUsernameFromToken(String token);

    Long extractUserIdFromToken(String token);

    Boolean isTokenValid(String token);
}
