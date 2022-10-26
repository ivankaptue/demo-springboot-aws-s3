package com.klid.demo_springboot_aws_s3.service;

import com.klid.demo_springboot_aws_s3.exception.StorageServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
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

    public String saveFile(String content) {
        String key = String.format("%s.txt", UUID.randomUUID());

        logger.info("Key : " + key);

        var objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .build();

        var file = createFileAndWriteContent(content);

        var requestBody = RequestBody.fromFile(file);

        s3Client.putObject(objectRequest, requestBody);

        deleteLocalFile(file);

        logger.info("Done");

        return key;
    }

    public String getFileContent(String key) {
        var objectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        try {
            var objectResponse = s3Client.getObject(objectRequest);
            return new String(objectResponse.readAllBytes());
        } catch (NoSuchKeyException ex) {
            logger.error("Key does not exist");
            throw new StorageServiceException(ex, HttpStatus.NOT_FOUND);
        } catch (IOException ex) {
            logger.error("IO exception");
            throw new StorageServiceException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteLocalFile(File file) {
        try {
            Files.delete(file.toPath());
        } catch (Exception e) {
            logger.error("Error on delete file", e);
        }
    }

    private File createFileAndWriteContent(String fileContent) {
        var file = new File(String.format("%s.txt", System.currentTimeMillis()));

        try (var outputStream = new FileOutputStream(file)) {
            outputStream.write(fileContent.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return file;
    }
}
