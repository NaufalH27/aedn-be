package com.aedn.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "website_profile")
public class WebsiteProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "photo_profile", columnDefinition = "TEXT")
    private String photoProfile;

    @Column(name = "personal_description", columnDefinition = "TEXT")
    private String personalDescription;

    private String whatsappUrl;
    private String instagramUrl;
    private String facebookUrl;
    private String vgenUrl;
    private String discordUrl;
    private String twitterUrl;

    private String email;
}
