package com.aedn.security;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.aedn.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    private final JwtConfig jwtConfig;

    public String generateToken(UUID id, String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(id.toString())
                .claim("username", username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtConfig.getExpirationTime()))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public Claims parseToken(String bearerToken) {
        String token = bearerToken.replace("Bearer ", "").trim();

        return Jwts.parser()
            .verifyWith(jwtConfig.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

}
