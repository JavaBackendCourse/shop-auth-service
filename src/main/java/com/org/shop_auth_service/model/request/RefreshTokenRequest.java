package com.org.shop_auth_service.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RefreshTokenRequest(
        @NotNull(message = "Refresh token is required")
        String refreshToken
) {
}
