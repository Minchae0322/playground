package com.example.playgroundmanage.exception;

public class TokenNotValidException  extends RuntimeException{
    private static final String MESSAGE = "Token 无效";
    public TokenNotValidException() {
        super(MESSAGE);
    }
}
