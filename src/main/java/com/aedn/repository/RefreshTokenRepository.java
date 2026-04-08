package com.aedn.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.aedn.entity.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String Token);
}
