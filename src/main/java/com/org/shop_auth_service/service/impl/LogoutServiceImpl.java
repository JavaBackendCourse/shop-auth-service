package com.org.shop_auth_service.service.impl;

import com.org.shop_auth_service.service.LogoutService;
import com.org.shop_auth_service.service.TokenManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutService {

    private final TokenManagerService tokenManagerService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("[LogoutService][logout] стартовал");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // без "Bearer "

            try {
                Long userId = tokenManagerService.getUserIdFromAccessToken(token);
                tokenManagerService.deleteAllTokensByUserId(userId);
                SecurityContextHolder.clearContext();
                log.info("[LogoutService][logout] успешно отработал");
            } catch (Exception e) {
                log.error("[LogoutService][logout] получена ошибка: {}", e.getMessage(), e);
            }
        }
    }
}