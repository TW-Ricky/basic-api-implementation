package com.thoughtworks.rslist.exception;

public class VoteNumberOverUsersOwnException extends RuntimeException {
    private String message;
    public VoteNumberOverUsersOwnException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
