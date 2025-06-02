package com.org.shop_auth_service.service.impl;

import com.org.shop_auth_service.model.request.CreateUserRequest;
import com.org.shop_auth_service.model.request.LoginRequest;
import com.org.shop_auth_service.model.request.RefreshTokenRequest;
import com.org.shop_auth_service.model.request.RegistrationRequest;
import com.org.shop_auth_service.model.token.RefreshToken;
import com.org.shop_auth_service.model.user.UserData;
import com.org.shop_auth_service.model.user.UserDetailsAdapter;
import com.org.shop_auth_service.model.user.UserRole;
import com.org.shop_auth_service.service.AuthService;
import com.org.shop_auth_service.service.TokenManagerService;
import com.org.shop_auth_service.service.UserService;
import com.org.shop_auth_service.utils.converters.JsonConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final String PASSWORD_MASK = "******";

    private final UserService userService;

    private final TokenManagerService tokenManagerService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Pair<String, String> register(RegistrationRequest registrationRequest) {
        log.info("[AuthServiceImpl][register] стартовал: {}", JsonConverter.toJson(
                registrationRequest.toBuilder().password(PASSWORD_MASK).build()).orElse(""));

        try {
            UserData createdUser = userService.createUser(
                    CreateUserRequest.builder()
                            .firstName(registrationRequest.firstName())
                            .lastName(registrationRequest.lastName())
                            .middleName(registrationRequest.middleName())
                            .birthDate(registrationRequest.birthDate())
                            .email(registrationRequest.email())
                            .phoneNumber(registrationRequest.phoneNumber())
                            .role(UserRole.valueOf(registrationRequest.role()))
                            .encodedPassword(passwordEncoder.encode(registrationRequest.password()))
                            .build()
            );

            //Generating tokens
            Pair<String, String> tokens = tokenManagerService.generateAccessAndRefreshTokens(
                    createdUser.getId(),
                    UserDetailsAdapter.builder().user(createdUser).build()
            );

            log.info("[AuthServiceImpl][register] успещно отработал");

            return tokens;
        } catch (Exception e) {
            log.error("[AuthServiceImpl][register] получена ошибка: {}", e.getMessage(), e);

            throw new RuntimeException("Ошибка при регистрации нового пользователя!");
        }
    }

    @Override
    @Transactional
    public Pair<String, String> login(LoginRequest loginRequest) {
        log.info("[AuthServiceImpl][login] стартовал: {}", JsonConverter.toJson(
                loginRequest.toBuilder().password(PASSWORD_MASK).build()).orElse(""));

        try {
            UserData user = userService.getUserByEmail(loginRequest.email());

            Pair<String, String> tokens = tokenManagerService.generateAccessAndRefreshTokens(
                    user.getId(),
                    UserDetailsAdapter.builder().user(user).build()
            );

            log.info("[AuthServiceImpl][login] успещно отработал");

            return tokens;
        } catch (IllegalArgumentException e) {
            log.error("[AuthServiceImpl][login] получена ошибка IllegalArgumentException: {}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            log.error("[AuthServiceImpl][login] получена ошибка: {}", e.getMessage(), e);

            throw new RuntimeException("Ошибка при входе пользователя в систему!");
        }
    }

    @Override
    public Pair<String, String> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("[AuthServiceImpl][refreshToken] стратовал");

        try {
            RefreshToken refreshToken = tokenManagerService.getRefreshTokenByToken(refreshTokenRequest.refreshToken())
                    .orElseThrow(() -> new IllegalArgumentException("Невалидный рефреш токен!"));

            tokenManagerService.verifyRefreshToken(refreshToken);

            UserData user = userService.getUserById(refreshToken.getUserId());

            Pair<String, String> tokens = tokenManagerService.generateAccessAndRefreshTokens(
                    user.getId(),
                    UserDetailsAdapter.builder().user(user).build()
            );

            log.info("[AuthServiceImpl][refreshToken] успешно отработал");

            return tokens;
        } catch (IllegalArgumentException e) {
            log.error("[AuthServiceImpl][refreshToken] получена ошибка IllegalArgumentException: {}", e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            log.error("[AuthServiceImpl][refreshToken] получена ошибка: {}", e.getMessage(), e);

            throw new RuntimeException("Ошибка при рефреше токена пользователя!");
        }
    }
}
