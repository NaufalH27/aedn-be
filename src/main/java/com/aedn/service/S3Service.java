package com.aedn.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aedn.dto.S3UploadPictureDto;
import com.aedn.dto.S3UploadPictureRequestDto;
import com.aedn.infra.storage.S3PresignedUrlProvider;
import com.aedn.mapper.S3PictureMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3PresignedUrlProvider s3PresignedUrlProvider;
    private final S3PictureMapper s3PictureMapper;

    public S3UploadPictureDto generateUploadPrivateBucketUrl(S3UploadPictureRequestDto reqDto, String bucket, String prefix) {
        String filename = UUID.randomUUID().toString() + "." + reqDto.getImageExtension();
        String s3SignedUrl = s3PresignedUrlProvider.generateUploadUrl(bucket, prefix + "/" + filename, reqDto.getImageExtension());
        S3UploadPictureDto dto = s3PictureMapper.toDto(s3SignedUrl, prefix, filename);
        dto.setUrl(s3PresignedUrlProvider.generateViewUrl(bucket, prefix + "/" + filename));
        return dto;
    }

    public S3UploadPictureDto generateUploadPublicBucketUrl(S3UploadPictureRequestDto reqDto, String prefix) {
        String filename = UUID.randomUUID().toString() + "." + reqDto.getImageExtension();
        String s3SignedUrl = s3PresignedUrlProvider.generateUploadUrl("public", prefix + "/" + filename, reqDto.getImageExtension());
        S3UploadPictureDto dto = s3PictureMapper.toDto(s3SignedUrl, prefix, filename);
        return dto;
    }

}
