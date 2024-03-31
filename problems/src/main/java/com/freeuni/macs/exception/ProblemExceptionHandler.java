package com.freeuni.macs.exception;

import com.freeuni.macs.model.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("com.freeuni.macs")
@Slf4j
public class ProblemExceptionHandler {

    @Value("Exception in ${spring.application.name}: ")
    private String errorPrefix;

    @ExceptionHandler(ProblemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse onProblemNotFoundException(ProblemNotFoundException ex) {
        log.error(ex.getClass().getName() + ": " + ex.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), errorPrefix + ex.getMessage());
    }
}