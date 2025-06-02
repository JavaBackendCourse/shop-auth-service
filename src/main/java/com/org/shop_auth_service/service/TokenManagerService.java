package com.org.shop_auth_service.service;

import com.org.shop_auth_service.model.token.RefreshToken;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface TokenManagerService {
    Pair<String, String> generateAccessAndRefreshTokens(Long userId, UserDetails userDetails) throws Exception;

    void deleteAllTokensByUserId(Long userId);

    void verifyRefreshToken(RefreshToken refreshToken);

    Optional<RefreshToken> getRefreshTokenByToken(String token);

    Long getUserIdFromAccessToken(String token);

    Boolean doesAccessTokenExist(String token);
}
