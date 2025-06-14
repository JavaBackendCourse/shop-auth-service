package com.org.shop_auth_service.service.impl;

import com.org.shop_auth_service.model.token.RefreshToken;
import com.org.shop_auth_service.model.token.Token;
import com.org.shop_auth_service.model.token.TokenType;
import com.org.shop_auth_service.repository.RefreshTokenRepository;
import com.org.shop_auth_service.repository.TokenRepository;
import com.org.shop_auth_service.service.JwtService;
import com.org.shop_auth_service.service.TokenManagerService;
import lombok.Data;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@Service
public class TokenManagerServiceImpl implements TokenManagerService {
    @Value("${app.settings.security.refresh-token.expiration}")
    private String refreshTokenExpiration;

    private final JwtService jwtService;

    private final TokenRepository accessTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public Pair<String, String> generateAccessAndRefreshTokens(Long userId, UserDetails userDetails) throws Exception {
        deleteAllTokensByUserId(userId);

        String accessToken = jwtService.generateJwtToken(userDetails);
        String refreshToken = generateRefreshToken();

        return saveTokens(userId, new Pair<>(accessToken, refreshToken));
    }

    private Pair<String, String> saveTokens(
            Long userId,
            Pair<String, String> accessAndRefreshToken
    ) throws Exception {
        CompletableFuture<Token> accessTokenFuture = CompletableFuture.supplyAsync(() ->
                accessTokenRepository.save(
                        Token.builder()
                                .userId(userId)
                                .token(accessAndRefreshToken.a)
                                .tokenType(TokenType.BEARER)
                                .expired(false)
                                .revoked(false)
                                .build()
                )
        );

        CompletableFuture<RefreshToken> refreshTokenFuture = CompletableFuture.supplyAsync(() ->
                refreshTokenRepository.save(
                        RefreshToken.builder()
                                .userId(userId)
                                .token(accessAndRefreshToken.b)
                                .expiresAt(Instant.now().plusMillis(Long.parseLong(refreshTokenExpiration)))
                                .build()
                )
        );

        CompletableFuture.allOf(accessTokenFuture, refreshTokenFuture).join();

        return new Pair<>(accessTokenFuture.get().getToken(), refreshTokenFuture.get().getToken());
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void deleteAllTokensByUserId(Long userId) {
        CompletableFuture<Void> deleteAccessTokenFuture = CompletableFuture.runAsync(() ->
                accessTokenRepository.deleteAllByUserId(userId)
        );

        CompletableFuture<Void> deleteRefreshTokenFuture = CompletableFuture.runAsync(() ->
                refreshTokenRepository.deleteAllByUserId(userId)
        );

        CompletableFuture.allOf(deleteAccessTokenFuture, deleteRefreshTokenFuture).join();
    }

    @Override
    public void verifyRefreshToken(RefreshToken refreshToken) {
        if (refreshToken.getExpiresAt().compareTo(Instant.now()) < 0) {
            throw new RuntimeException("Refresh token expired");
        }
    }

    @Override
    public Optional<RefreshToken> getRefreshTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public Long getUserIdFromAccessToken(String token) {
        return jwtService.extractUserIdFromToken(token);
    }

    @Override
    public Boolean doesAccessTokenExist(String token) {
        return accessTokenRepository.findByToken(token).isPresent();
    }
}
