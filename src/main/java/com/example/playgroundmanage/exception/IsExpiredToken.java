package com.example.playgroundmanage.exception;

public class IsExpiredToken extends RuntimeException{
    private static final String MESSAGE = "Token 过期了";
    public IsExpiredToken() {
        super(MESSAGE);
    }
}
