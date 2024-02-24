package com.ivan.restapplication.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@Builder
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationException extends RuntimeException{

    HttpStatus statusCode;
    String message;
    String exceptionMessage;
    LocalDateTime timestamp;

}
