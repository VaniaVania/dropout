package com.ivan.restapplication.exception;

import com.ivan.restapplication.exception.response.ExceptionMessageConstant;
import com.ivan.restapplication.exception.status.Status400BadRequest;

public class NotListeningUserException extends Status400BadRequest {

    public NotListeningUserException() {
        super(ExceptionMessageConstant.USER_NOT_LISTENING);
    }
}
