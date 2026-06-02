package com.aedn.dto;


import com.aedn.entity.WebsiteProfile;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContactInfoDto {

    String email;
    String whatsappUrl;
    String instagramUrl;
    String facebookUrl;
    private String twitterUrl;

    public static ContactInfoDto fromEntity(WebsiteProfile entity) {
        if (entity == null) {
            return null;
        }

        return ContactInfoDto.builder()
            .email(entity.getEmail())
            .whatsappUrl(entity.getWhatsappUrl())
            .instagramUrl(entity.getInstagramUrl())
            .facebookUrl(entity.getFacebookUrl())
            .twitterUrl(entity.getTwitterUrl())
            .build();
    }
}
