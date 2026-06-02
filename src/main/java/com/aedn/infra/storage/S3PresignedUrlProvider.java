package com.aedn.infra.storage;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aedn.exception.ObjectStorageUnavailableException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlProvider {

    private final Optional<S3Presigner> s3Presigner;

    private S3Presigner getPresigner() {
        return s3Presigner.orElseThrow(
            () -> new ObjectStorageUnavailableException("Unconfigurable Object Storage")
        );
    }

    public String generateUploadUrl(String bucket, String key, String imageExtension) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(getContentType(imageExtension))
            .build();

        PutObjectPresignRequest presignRequest =
            PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1))
            .putObjectRequest(putObjectRequest)
            .build();

        PresignedPutObjectRequest presignedRequest =
            getPresigner().presignPutObject(presignRequest);

        return presignedRequest.url().toString();

    }

    public String generateViewUrl(String bucket, String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        GetObjectPresignRequest presignRequest =
            GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(5))
            .getObjectRequest(getObjectRequest)
            .build();

        PresignedGetObjectRequest presignedRequest =
            getPresigner().presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

    public String generateDownloadUrl(
            String bucket,
            String key,
            String filename
            ) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .responseContentDisposition("attachment; filename=\"" + filename + "\"")
            .build();

        GetObjectPresignRequest presignRequest =
            GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1))
            .getObjectRequest(getObjectRequest)
            .build();

        return getPresigner()
            .presignGetObject(presignRequest)
            .url()
            .toString();
        }

    private static String getContentType(String extension) {
        Map<String, String> CONTENT_TYPES = Map.of(
                "png", "image/png",
                "jpg", "image/jpeg",
                "jpeg", "image/jpeg",
                "gif", "image/gif",
                "webp", "image/webp",
                "bmp", "image/bmp",
                "tiff", "image/tiff",
                "avif", "image/avif"
                );
        return CONTENT_TYPES.get(extension.toLowerCase());
    }
}
