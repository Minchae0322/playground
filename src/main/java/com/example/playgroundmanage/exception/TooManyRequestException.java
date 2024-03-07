package com.example.playgroundmanage.exception;

public class TooManyRequestException extends RuntimeException{
    private static final String MESSAGE = "请求频度多";
    public TooManyRequestException() {
        super(MESSAGE);
    }
}
