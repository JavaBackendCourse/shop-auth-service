package com.org.shop_auth_service.service;

import com.org.shop_auth_service.model.request.LoginRequest;
import com.org.shop_auth_service.model.request.RefreshTokenRequest;
import com.org.shop_auth_service.model.request.RegistrationRequest;
import org.antlr.v4.runtime.misc.Pair;

public interface AuthService {
    Pair<String, String> register(RegistrationRequest registrationRequest);

    Pair<String, String> login(LoginRequest loginRequest);

    Pair<String, String> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
