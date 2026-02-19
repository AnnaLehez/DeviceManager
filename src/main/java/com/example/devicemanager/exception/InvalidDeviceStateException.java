package com.example.devicemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidDeviceStateException extends RuntimeException {
    public InvalidDeviceStateException(String message) {
        super(message);
    }
}