package com.aedn.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@ConditionalOnProperty(name = "s3.enabled", havingValue = "true")
public class S3ClientConfig {

    @Value("${s3.endpoint:}")
    private String endpoint;

    @Value("${s3.region:}")
    private String region;

    @Value("${s3.secret:}")
    private String secretKey;

    @Value("${s3.access:}")
    private String accessKey;

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .endpointOverride(URI.create(this.endpoint))
            .region(Region.of(this.region))
            .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(this.accessKey, this.secretKey)
                        ))
            .serviceConfiguration(S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build())
            .build();
    }

    public String getEndpoint() {
        return endpoint;
    }
}
