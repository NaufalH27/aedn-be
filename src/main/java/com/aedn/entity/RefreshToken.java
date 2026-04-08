package com.aedn.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RefreshToken {
    
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable=false, unique =true)
    String token;

    @Column(nullable=false)
    UUID userId;

    @Column(nullable=false)
    Instant expiresAt;

    Instant createdAt = Instant.now();

    protected RefreshToken() {
    }

    public RefreshToken(String token, UUID userId, Instant expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

}
