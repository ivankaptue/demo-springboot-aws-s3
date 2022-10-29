package com.klid.demo_springboot_aws_s3.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Ivan Kaptue
 */
@ControllerAdvice
public class StorageServiceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(StorageServiceExceptionHandler.class);

    @ExceptionHandler(StorageServiceException.class)
    public ResponseEntity<String> handleStorageServiceException(StorageServiceException ex) {
        logger.error("StorageService Error", ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(String.format("%s", ex.getCause()));
    }
}
