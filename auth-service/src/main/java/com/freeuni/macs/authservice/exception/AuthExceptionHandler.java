package com.freeuni.macs.authservice.exception;

import com.freeuni.macs.authservice.model.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice("com.freeuni.macs.authservice")
@Slf4j
public class AuthExceptionHandler {

    @Value("Exception in ${spring.application.name}: ")
    private String errorPrefix;

    @ExceptionHandler(UserAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse onResourceNotFoundException(UserAuthException ex) {
        log.error(ex.getClass().getName() + ": " + ex.getMessage());
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), errorPrefix + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getClass().getName() + ": " + ex.getMessage());
        final List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError ->
                        fieldError.getField() + ": (" + fieldError.getRejectedValue() + ") " + ((fieldError.getDefaultMessage() == null) ? "" : fieldError.getDefaultMessage())
                )
                .toList();

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorPrefix + errorMessages);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse onUnexpectedException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorPrefix + "Unexpected error occurred");
    }
}