package com.javaweb.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TriggerNotFoundException extends RuntimeException {
    public TriggerNotFoundException (String message) {
        super(message);
    }
}
