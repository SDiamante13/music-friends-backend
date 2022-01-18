package com.kinandcarta.musicfriends.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(value = {ClientException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleClientException(Exception ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(value = {ServerException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    ErrorResponse handleServerException(Exception ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(value = {TokenFailureException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleTokenFailureException(Exception ex) {
        return buildErrorResponse(ex);
    }

    private ErrorResponse buildErrorResponse(Exception ex) {
        String type = ex.getClass().getSimpleName();
        String message = ex.getMessage();

        return new ErrorResponse(new Error(type, message));
    }
}
