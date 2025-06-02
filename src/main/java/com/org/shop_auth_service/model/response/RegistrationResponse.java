package com.org.shop_auth_service.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RegistrationResponse(
        @NotNull
        String accessToken,
        @NotNull
        String refreshToken
) {
}
