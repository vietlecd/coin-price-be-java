package com.javaweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {
    private class Err {
        public String msg;
        public String code;

        public Err(String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Err> handleHttpMessageNotReadable() {
        return new ResponseEntity<>(new Err("không tìm thấy dữ liệu trong Body"), HttpStatus.BAD_REQUEST);
    }
}
