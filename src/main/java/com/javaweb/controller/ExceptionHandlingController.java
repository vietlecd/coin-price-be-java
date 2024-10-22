package com.javaweb.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class ExceptionHandlingController {

    @Data
    @AllArgsConstructor
    private class Responses{
        private Date timestamp;
        private String status;
        private String message;
        private String path;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                          HttpServletRequest request) {
        return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "400",
                        e.getMessage(),
                        request.getRequestURL().toString())
                ,HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Responses> handleErr(NoHandlerFoundException e) {
        return new ResponseEntity<>(
                new Responses(
                        new Date(),
                        "404",
                        "Không tìm thấy api này",
                        e.getRequestURL().toString()
                ), HttpStatus.NOT_FOUND
        );
    }
}
