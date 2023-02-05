package com.ivan.restapplication.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalUnauthorizedUserExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<UnauthorizedUserResponse> handleException(UnauthorizedUserException e) {
        UnauthorizedUserResponse response = new UnauthorizedUserResponse("Person is not authorized!",
                new Date().getTime());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // Unauthorized - Status 401
    }

}
