package com.klid.demo_springboot_aws_s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.Duration;

/**
 * @author Ivan Kaptue
 */
@Configuration
public class StorageConfig {

    private AwsCredentials awsCredentials(String accessKey, String secret) {
        return new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return accessKey;
            }

            @Override
            public String secretAccessKey() {
                return secret;
            }
        };
    }

    private RetryPolicy retryPolicy() {
        int maxAttempts = 2;

        return RetryPolicy.builder()
            .numRetries(maxAttempts)
            .build();
    }

    private ClientOverrideConfiguration clientOverrideConfiguration() {
        Duration apiCallTimeout = Duration.ofSeconds(5);

        return ClientOverrideConfiguration.builder()
            .apiCallAttemptTimeout(apiCallTimeout)
            .apiCallTimeout(apiCallTimeout)
            .retryPolicy(retryPolicy())
            .build();
    }

    @Bean
    public S3Client s3Client(
        @Value("${app.aws.s3.region}") String region,
        @Value("${app.aws.s3.access-key}") String accessKey,
        @Value("${app.aws.s3.secret}") String secret
    ) {
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials(accessKey, secret)))
            .overrideConfiguration(clientOverrideConfiguration())
            .build();
    }
}
