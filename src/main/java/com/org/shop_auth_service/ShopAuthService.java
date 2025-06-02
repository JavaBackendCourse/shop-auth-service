package com.org.shop_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShopAuthService {

	public static void main(String[] args) {
		SpringApplication.run(ShopAuthService.class, args);
	}

}
