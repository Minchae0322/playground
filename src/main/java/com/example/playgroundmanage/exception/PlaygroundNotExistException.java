package com.example.playgroundmanage.exception;

public class PlaygroundNotExistException extends RuntimeException{
    private static final String MESSAGE = "运动场已禁用或不存在";
    public PlaygroundNotExistException() {
        super(MESSAGE);
    }
}
