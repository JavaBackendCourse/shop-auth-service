package com.org.shop_auth_service.feign;

import com.org.shop_auth_service.config.feign.FeignClientInternalConfig;
import com.org.shop_auth_service.model.request.CreateUserRequest;
import com.org.shop_auth_service.model.user.UserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        url = "${app.settings.feign.routes.user-service}",
        configuration = FeignClientInternalConfig.class)
public interface UserServiceFeign {
    @GetMapping("/internal/users/{id}")
    ResponseEntity<UserData> getUserById(@PathVariable("id") Long id);

    @GetMapping("/internal/users/email/{email}")
    ResponseEntity<UserData> getUserByEmail(@PathVariable("email") String email);

    @PostMapping("/internal/users")
    ResponseEntity<UserData> createUser(@RequestBody CreateUserRequest request);
}
