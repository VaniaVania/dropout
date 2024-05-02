package com.ivan.restapplication.exception.handler;

import com.ivan.restapplication.exception.ApplicationExceptionResponse;
import com.ivan.restapplication.exception.NotListeningUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = NotListeningUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApplicationExceptionResponse handleNotListeningUserException(NotListeningUserException e) {
        return ApplicationExceptionResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .exceptionMessage(e.getMessage())
                .build();
    }
}
