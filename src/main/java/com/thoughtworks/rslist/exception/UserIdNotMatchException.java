package com.thoughtworks.rslist.exception;

public class UserIdNotMatchException extends RuntimeException {
    private String message;
    public UserIdNotMatchException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
