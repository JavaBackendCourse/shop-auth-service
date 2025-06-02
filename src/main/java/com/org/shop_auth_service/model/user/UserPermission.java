package com.org.shop_auth_service.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPermission {
    ; // TODO add permissions in the future
    private final String permission;
}
