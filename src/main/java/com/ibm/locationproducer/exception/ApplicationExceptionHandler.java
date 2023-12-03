package com.ibm.locationproducer.exception;


import org.apache.kafka.common.errors.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(LocationApiException.class)
    public ResponseEntity<?> handleBookAPIException(LocationApiException locationApiException){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message", locationApiException.getMessage());
        errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return ResponseEntity.ok(errorMap);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleKafkaAuthentication(AuthenticationException authenticationException){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message", authenticationException.getMessage());
        errorMap.put("status", HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.ok(errorMap);
    }

    @ExceptionHandler(AzureStorageException.class)
    public ResponseEntity<?> handleBlobStorageException(AzureStorageException storageException){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message", storageException.getMessage());
        errorMap.put("status", HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.ok(errorMap);
    }

}
