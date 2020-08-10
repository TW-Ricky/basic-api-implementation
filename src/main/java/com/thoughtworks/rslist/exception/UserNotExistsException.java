package com.thoughtworks.rslist.exception;

public class UserNotExistsException extends RuntimeException{
    private String message;
    public UserNotExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
