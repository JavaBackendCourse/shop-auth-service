package com.org.shop_auth_service.config.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientInternalConfig {
    @Value("${app.settings.security.internal-token.value}")
    private String internalToken;

    @Bean
    public RequestInterceptor internalTokenInterceptor() {
        return requestTemplate -> requestTemplate.header("X-Internal-Token", internalToken);
    }
}
