package com.thoughtworks.rslist.exception;

public class TimeErrorException extends RuntimeException {
    private String message;
    public TimeErrorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
