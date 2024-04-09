package com.example.playgroundmanage.exception;

public class UserNotValidException extends RuntimeException {
    private static final String MESSAGE = "用户无效";
    public UserNotValidException() {
        super(MESSAGE);
    }
}
