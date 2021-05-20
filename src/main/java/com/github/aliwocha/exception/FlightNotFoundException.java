package com.github.aliwocha.exception;

public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(final String message) {
        super(message);
    }
}
