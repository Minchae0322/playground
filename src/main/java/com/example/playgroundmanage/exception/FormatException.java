package com.example.playgroundmanage.exception;

public class FormatException extends RuntimeException{
    private static final String MESSAGE = "형식에 맞지 않습니다.";
    public FormatException() {
        super(MESSAGE);
    }
}
