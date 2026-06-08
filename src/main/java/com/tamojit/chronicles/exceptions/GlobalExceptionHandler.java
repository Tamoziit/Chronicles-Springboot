package com.tamojit.chronicles.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;

// if a controller is protected under authentication middleware, any exception thrown by the controller is intercepted by GlobalExceptionHandler & handled by it, to throw an appropriate custom mssg.
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        String message = "You are not authorized to perform this operation";
        return new ResponseEntity<>(message, FORBIDDEN);
    }
}
