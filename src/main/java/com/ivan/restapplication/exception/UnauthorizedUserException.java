package com.ivan.restapplication.exception;

import com.ivan.restapplication.exception.response.ExceptionMessageConstant;

public class UnauthorizedUserException extends RuntimeException{
    public UnauthorizedUserException() {
        super(ExceptionMessageConstant.USER_NOT_AUTHORIZED);
    }
}
