package com.example.playgroundmanage.exception;

public class TokenNotValidException  extends RuntimeException{
    private static final String MESSAGE = "유효하지 않습니다.";
    public TokenNotValidException() {
        super(MESSAGE);
    }
}
