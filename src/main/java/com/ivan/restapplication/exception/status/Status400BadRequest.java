package com.ivan.restapplication.exception.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Status400BadRequest extends RuntimeException{
    public Status400BadRequest(String message) {
        super(message);
    }
}
