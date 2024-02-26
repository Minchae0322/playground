package com.example.playgroundmanage.exception;

public class GameNotExistException extends RuntimeException {
    private static final String MESSAGE = "不存在的比赛";
    public GameNotExistException() {
        super(MESSAGE);
    }
}
