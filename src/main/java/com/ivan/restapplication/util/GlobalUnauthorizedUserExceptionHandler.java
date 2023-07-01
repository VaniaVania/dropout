package com.ivan.restapplication.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;

@ControllerAdvice
public class GlobalUnauthorizedUserExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<UnauthorizedUserResponse> handleException(UnauthorizedUserException e) {
        UnauthorizedUserResponse response = new UnauthorizedUserResponse("Person is not authorized!",
                new Date().getTime());
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // Unauthorized - Status 401
    }

    @ExceptionHandler
    private ResponseEntity<NotListeningUserResponse> handleException(NotListeningUserException e) {
        NotListeningUserResponse response = new NotListeningUserResponse("Please, start to listen music :-)",
                new Date().getTime());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
