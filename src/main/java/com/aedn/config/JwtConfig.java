package com.aedn.config;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class JwtConfig {
    private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);

    @Value("${jwt.secret:}")
    private String key;

    @Value("${jwt.expiration:5m}")
    private Duration expirationTime;

    @Value("${jwt.refresh.expiration:30d}")
    private Duration refreshTokenExpirationTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (key == null || key.isBlank()) {
            this.key = generateSecret();
            log.warn("JWT secret not found in configuration. Generated a temporary one. if you see this warning in production, please set up your jwt secret key in .env. for example: JWT_SECRET_KEY=my-secret-key");
        }

        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid jwt.secret: must be a valid Base64-encoded key", e);
        }
    }

    private String generateSecret() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}

