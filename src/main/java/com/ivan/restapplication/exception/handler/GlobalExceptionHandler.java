package com.ivan.restapplication.exception.handler;

import com.ivan.restapplication.exception.ApplicationException;
import com.ivan.restapplication.exception.NotListeningUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NotListeningUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApplicationException handleNotListeningUserException(NotListeningUserException e) {
        return ApplicationException.builder()
                .message("Not valid data")
                .exceptionMessage(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
