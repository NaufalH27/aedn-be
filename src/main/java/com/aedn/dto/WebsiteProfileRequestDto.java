package com.aedn.dto;

import lombok.Data;

@Data
public class WebsiteProfileRequestDto {

    private String photoProfile;
    private String personalDescription;
    private String whatsappUrl;
    private String instagramUrl;
    private String facebookUrl;
    private String vgenUrl;
    private String discordUrl;
    private String twitterUrl;

    private String email;
}
