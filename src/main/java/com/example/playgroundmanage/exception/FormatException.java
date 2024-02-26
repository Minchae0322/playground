package com.example.playgroundmanage.exception;

public class FormatException extends RuntimeException{
    private static final String MESSAGE = "不符合格式 ";
    public FormatException() {
        super(MESSAGE);
    }
}
