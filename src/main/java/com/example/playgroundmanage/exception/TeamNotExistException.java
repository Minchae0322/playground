package com.example.playgroundmanage.exception;

public class TeamNotExistException extends RuntimeException{
    private static final String MESSAGE = "团队不存在";
    public TeamNotExistException() {
        super(MESSAGE);
    }
}
