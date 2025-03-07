package com.foodtaste.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "jwt") // Binds properties with prefix "jwt" (from application.properties or application.yml).
public class JWTConfig {

	private String secretKey;
	private String algorithm;
	private String expiration;
}
