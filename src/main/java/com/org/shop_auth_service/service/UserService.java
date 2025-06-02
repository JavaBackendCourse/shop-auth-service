package com.org.shop_auth_service.service;

import com.org.shop_auth_service.model.request.CreateUserRequest;
import com.org.shop_auth_service.model.user.UserData;

public interface UserService {
    UserData createUser(CreateUserRequest createUserRequest);

    UserData getUserByEmail(String email);

    UserData getUserById(Long id);
}
