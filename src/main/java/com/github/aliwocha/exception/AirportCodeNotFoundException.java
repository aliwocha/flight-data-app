package com.github.aliwocha.exception;

public class AirportCodeNotFoundException extends RuntimeException {

    public AirportCodeNotFoundException(final String message) {
        super(message);
    }
}
