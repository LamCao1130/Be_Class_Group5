package com.he181464.be_class.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandling {

    @ExceptionHandler(ObjectExistingException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectExistingException e) {
        log.error("Service - handleObjectNotFoundException {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Service - handleIllegalArgumentException {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
