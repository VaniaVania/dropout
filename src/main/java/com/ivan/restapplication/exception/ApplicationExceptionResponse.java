package com.ivan.restapplication.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter

public class ApplicationExceptionResponse{

    final Integer statusCode;
    final String message;
    final String exceptionMessage;
    final LocalDateTime timestamp;

}
