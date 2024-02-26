package com.example.playgroundmanage.exception;

public class ExistUserException extends RuntimeException{

    private static final String MESSAGE = "已注册的用户";
    public ExistUserException() {
        super(MESSAGE);
    }
}
