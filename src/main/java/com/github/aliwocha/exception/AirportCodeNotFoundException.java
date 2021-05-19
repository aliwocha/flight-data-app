package com.github.aliwocha.exception;

public class AirportCodeNotFoundException extends RuntimeException {

    public AirportCodeNotFoundException(String message) {
        super(message);
    }
}
