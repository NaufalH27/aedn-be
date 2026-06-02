package com.aedn.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aedn.dto.S3UploadPictureDto;
import com.aedn.exception.ObjectStorageUnavailableException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3PictureMapper {

    @Value("${s3.endpoint.public:}")
    private String s3Endpoint;

    public S3UploadPictureDto toDto(String s3SignedUrl, String prefix, String filename) {
        if (this.s3Endpoint.isBlank()) {
            throw new ObjectStorageUnavailableException("Unconfigurable Object Storage endpoint");
        }

        S3UploadPictureDto picture = new S3UploadPictureDto();
        picture.setS3SignedUrl(s3SignedUrl);

        picture.setFilename(filename);
        picture.setUrl(s3Endpoint + "/"+ prefix + "/" + filename);
        picture.setKey(prefix + "/" + filename);

        return picture;

    }

}
