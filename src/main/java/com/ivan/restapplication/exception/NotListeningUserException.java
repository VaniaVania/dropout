package com.ivan.restapplication.exception;

import com.ivan.restapplication.exception.response.ExceptionMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotListeningUserException extends ApplicationException{

    public NotListeningUserException(String message) {
        super(HttpStatus.BAD_REQUEST, message, ExceptionMessageConstant.USER_NOT_LISTENING, LocalDateTime.now());
    }
}
