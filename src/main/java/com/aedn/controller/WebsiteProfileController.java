package com.aedn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.common.ApiResponse;
import com.aedn.dto.ContactInfoDto;
import com.aedn.dto.S3UploadPictureDto;
import com.aedn.dto.S3UploadPictureRequestDto;
import com.aedn.dto.WebsiteProfileDto;
import com.aedn.dto.WebsiteProfileRequestDto;
import com.aedn.service.S3Service;
import com.aedn.service.WebsiteProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WebsiteProfileController {

    private final WebsiteProfileService service;
    private final S3Service s3Service;

    @GetMapping("/website-profile")
    public ResponseEntity<ApiResponse<WebsiteProfileDto>> get() {
        return ResponseEntity.ok(ApiResponse.success("Get Profile Success", service.get()));
    }

    @GetMapping("website-profile/contact")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ContactInfoDto>> getContact() {
        return ResponseEntity.ok(ApiResponse.success("Get Profile Success", service.getContact()));
    }

    @PutMapping("/website-profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WebsiteProfileDto>> post(@RequestBody WebsiteProfileRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Update Profile Success", service.update(dto)));
    }
    
    @PostMapping("/website-profile/profile-picture/signed-url")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<S3UploadPictureDto>> getUploadUrl(@Valid @RequestBody S3UploadPictureRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Get Url Success", s3Service.generateUploadPublicBucketUrl(dto, "profile-picture")));
    }
}
