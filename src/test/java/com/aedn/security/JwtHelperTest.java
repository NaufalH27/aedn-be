package com.aedn.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.aedn.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

class JwtHelperTest {

    @Mock
    private JwtConfig jwtConfig;
    private JwtHelper jwtHelper;

    @BeforeEach
    void setUp() {
        this.jwtConfig = new JwtConfig(); 
        jwtConfig.setKey("uvlEeKcfCmGtkcXYs7VwVvvvcKcLJIQREJaId91sFc93/leLPUz/bVI8dDBNg8IhPY5RJzTGV5EmmPL7dPiMXw==");
        jwtConfig.setExpirationTime(3600);
        jwtConfig.init();
        this.jwtHelper = new JwtHelper(this.jwtConfig);

        assertNotNull(jwtConfig.getSecretKey());
        assertNotNull(jwtConfig.getSecretKey().getEncoded());
        assertTrue(jwtConfig.getSecretKey().getEncoded().length > 0);
    }

    @Test
    void validJwt() {
        String username = "amogus67";
        UUID id = UUID.randomUUID();
        String token = jwtHelper.generateToken(id, username);

        assertFalse(token.isBlank());
        assertNotNull(token);
        Claims claims = jwtHelper.parseToken("Bearer " + token);
        assertTrue(claims.getSubject().equals(id.toString()));
        assertNotNull(claims.getIssuedAt());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void malformedToken() {
        String invalidToken = "Bearer this.is.not.a.valid.jwt";

        assertThrows(JwtException.class, () -> jwtHelper.parseToken(invalidToken));
    }

    @Test
    void expiredJwt() throws InterruptedException {
        this.jwtConfig.setExpirationTime(1); 
        String token = jwtHelper.generateToken(UUID.randomUUID(), "amogus67");
        Thread.sleep(100); 
        assertThrows(ExpiredJwtException.class, () -> jwtHelper.parseToken("Bearer " + token));
    }
}
