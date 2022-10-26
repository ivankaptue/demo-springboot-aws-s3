package com.klid.demo_springboot_aws_s3.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Ivan Kaptue
 */
public class StorageServiceException extends RuntimeException {

    private HttpStatus httpStatus;

    public StorageServiceException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }
}
