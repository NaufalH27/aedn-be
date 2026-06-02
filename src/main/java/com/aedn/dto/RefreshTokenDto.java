package com.aedn.dto;

import java.time.Instant;
import java.util.UUID;

import com.aedn.entity.RefreshToken;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenDto {
    private UUID id;
    private String rawToken;
    private UUID userId;
    private Instant expiresAt;

    public static RefreshTokenDto fromEntity(RefreshToken entity, String rawToken) {
        RefreshTokenDto dto = new RefreshTokenDto();
        dto.setId(entity.getId());
        dto.setRawToken(rawToken);
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setUserId(entity.getUserId());
        return dto;
    }

}
