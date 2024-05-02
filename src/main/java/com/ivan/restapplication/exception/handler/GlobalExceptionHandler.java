package com.ivan.restapplication.exception.handler;

import com.ivan.restapplication.exception.ApplicationExceptionResponse;
import com.ivan.restapplication.exception.NotListeningUserException;
import com.ivan.restapplication.exception.UnauthorizedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = NotListeningUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApplicationExceptionResponse handleNotListeningUserException(NotListeningUserException e) {
        return ApplicationExceptionResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .exceptionMessage(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = UnauthorizedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApplicationExceptionResponse handleUnauthorizedUserException(UnauthorizedUserException e) {
        return ApplicationExceptionResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .exceptionMessage(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
