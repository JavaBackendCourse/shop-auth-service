package com.org.shop_auth_service.service.impl;

import com.org.shop_auth_service.feign.UserServiceFeign;
import com.org.shop_auth_service.model.request.CreateUserRequest;
import com.org.shop_auth_service.model.user.UserData;
import com.org.shop_auth_service.service.UserService;
import com.org.shop_auth_service.utils.converters.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceFeign userServiceFeign;


    @Override
    public UserData createUser(CreateUserRequest createUserRequest) {
        try {
            log.info("[UserServiceImpl][createUser] стратовал: {}", JsonConverter.toJson(createUserRequest).orElse(""));
            var response = userServiceFeign.createUser(createUserRequest).getBody();
            log.info("[UserServiceImpl][createUser] успешно отработал: {}", JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[UserServiceImpl][createUser] получена ошибка: {}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public UserData getUserByEmail(String email) {
        try {
            log.info("[UserServiceImpl][getUserByEmail][email={}] стратовал", email);
            var response = userServiceFeign.getUserByEmail(email).getBody();
            log.info("[UserServiceImpl][getUserByEmail][email={}] успешно отработал: {}", email, JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[UserServiceImpl][getUserByEmail][email={}] получена ошибка: {}", email, e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public UserData getUserById(Long id) {
        try {
            log.info("[UserServiceImpl][getUserById][id={}] стратовал: {}", id, JsonConverter.toJson(id).orElse(""));
            var response = userServiceFeign.getUserById(id).getBody();
            log.info("[UserServiceImpl][getUserById][id={}] успешно отработал: {}", id, JsonConverter.toJson(response).orElse(""));

            return response;
        } catch (Exception e) {
            log.error("[UserServiceImpl][getUserById][id={}] получена ошибка: {}", id, e.getMessage(), e);

            throw e;
        }
    }
}
