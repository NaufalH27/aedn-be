package com.aedn.dto;

import com.aedn.entity.WebsiteProfile;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WebsiteProfileDto {

    String photoProfile;
    String personalDescription;
    String email;
    String instagramUrl;
    String facebookUrl;
    String whatsappUrl;
    private String vgenUrl;
    private String discordUrl;
    private String twitterUrl;

    public static WebsiteProfileDto fromEntity(WebsiteProfile entity) {
        if (entity == null) {
            return null;
        }

        return WebsiteProfileDto.builder()
                .photoProfile(entity.getPhotoProfile())
                .personalDescription(entity.getPersonalDescription())
                .email(entity.getEmail())
                .instagramUrl(entity.getInstagramUrl())
                .facebookUrl(entity.getFacebookUrl())
                .vgenUrl(entity.getVgenUrl())
                .discordUrl(entity.getDiscordUrl())
                .twitterUrl(entity.getTwitterUrl())
                .build();
    }
    public static WebsiteProfileDto fromEntityAll(WebsiteProfile entity) {
        if (entity == null) {
            return null;
        }

        return WebsiteProfileDto.builder()
                .photoProfile(entity.getPhotoProfile())
                .personalDescription(entity.getPersonalDescription())
                .email(entity.getEmail())
                .instagramUrl(entity.getInstagramUrl())
                .facebookUrl(entity.getFacebookUrl())
                .whatsappUrl(entity.getWhatsappUrl())
                .vgenUrl(entity.getVgenUrl())
                .discordUrl(entity.getDiscordUrl())
                .twitterUrl(entity.getTwitterUrl())
                .build();
    }
}
