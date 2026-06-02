package com.aedn.service;

import org.springframework.stereotype.Service;

import com.aedn.dto.ContactInfoDto;
import com.aedn.dto.WebsiteProfileDto;
import com.aedn.dto.WebsiteProfileRequestDto;
import com.aedn.entity.WebsiteProfile;
import com.aedn.repository.WebsiteProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebsiteProfileService {

    private final WebsiteProfileRepository websiteProfileRepository;

    public WebsiteProfileDto get() {
        WebsiteProfile profile = websiteProfileRepository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Website Profile is Unavailable"));

        return WebsiteProfileDto.fromEntity(profile);
    }

    public ContactInfoDto getContact() {
        WebsiteProfile profile = websiteProfileRepository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Website Profile is Unavailable"));

        return ContactInfoDto.fromEntity(profile);
    }

    public WebsiteProfileDto update(WebsiteProfileRequestDto dto) {
        WebsiteProfile profile = websiteProfileRepository.findFirstBy()
                .orElseGet(() -> new WebsiteProfile());

        profile.setPhotoProfile(dto.getPhotoProfile());
        profile.setPersonalDescription(dto.getPersonalDescription());
        profile.setEmail(dto.getEmail());
        profile.setWhatsappUrl(dto.getWhatsappUrl());
        profile.setInstagramUrl(dto.getInstagramUrl());
        profile.setFacebookUrl(dto.getFacebookUrl());
        profile.setVgenUrl(dto.getVgenUrl());
        profile.setDiscordUrl(dto.getDiscordUrl());
        profile.setTwitterUrl(dto.getTwitterUrl());

        profile = websiteProfileRepository.save(profile);

        return WebsiteProfileDto.fromEntity(profile);
    }
}
