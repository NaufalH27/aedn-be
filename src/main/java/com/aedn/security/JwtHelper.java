package com.aedn.security;

import org.springframework.stereotype.Component;

import com.aedn.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    private final JwtConfig jwtConfig;

    public Claims parseToken(String bearerToken) {
        String token = bearerToken.replace("Bearer ", "").trim();

        return Jwts.parser()
            .verifyWith(jwtConfig.getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

}
