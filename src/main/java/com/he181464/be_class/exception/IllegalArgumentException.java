package com.he181464.be_class.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestControllerAdvice;


public class IllegalArgumentException extends RuntimeException {

    public IllegalArgumentException(String message) {
        super(message);
    }
}
