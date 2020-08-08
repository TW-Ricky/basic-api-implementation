package com.thoughtworks.rslist.exception;

public class RsEventNotValidException extends RuntimeException{
    private String message;

    public RsEventNotValidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
