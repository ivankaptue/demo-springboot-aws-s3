package com.klid.demo_springboot_aws_s3.service;

import com.klid.demo_springboot_aws_s3.exception.StorageServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Ivan Kaptue
 */
@Service
public class StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private final S3Client s3Client;
    private final String bucket;

    public StorageService(
        S3Client s3Client,
        @Value("${app.aws.s3.bucket}") String bucket
    ) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    public String saveFile(MultipartFile multipartFile) {
        logger.info("Save file to S3 bucket");

        try {

            String key = String.format("%s.txt", UUID.randomUUID());

            var objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .build();

            var requestBody = RequestBody.fromBytes(multipartFile.getBytes());

            s3Client.putObject(objectRequest, requestBody);

            logger.info("File saved to S3 bucket");

            return key;
        } catch (IOException ex) {
            throw new StorageServiceException("Error when getting content of multipart file", ex, HttpStatus.BAD_REQUEST);
        } catch (SdkClientException ex) {
            throw new StorageServiceException("Client Error when calling S3 Service", ex, HttpStatus.BAD_REQUEST);
        } catch (S3Exception ex) {
            throw new StorageServiceException("Server Error when calling S3 Service", ex, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public String getFileContent(String key) {
        logger.info("Get file content from S3 bucket");

        try {
            var objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
            var objectResponse = s3Client.getObject(objectRequest);

            var content = new String(objectResponse.readAllBytes(), StandardCharsets.UTF_8);

            logger.info("File content found and returned from S3 bucket");

            return content;
        } catch (NoSuchKeyException ex) {
            throw new StorageServiceException("Key does not exist", ex, HttpStatus.NOT_FOUND);
        } catch (SdkClientException ex) {
            throw new StorageServiceException("Client Error when calling S3 Service", ex, HttpStatus.BAD_REQUEST);
        } catch (S3Exception ex) {
            throw new StorageServiceException("Server Error when calling S3 Service", ex, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IOException ex) {
            throw new StorageServiceException("Error when getting objectResponse content", ex, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
