package com.example.playgroundmanage.exception;

public class GameNotExistException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 게임입니다.";
    public GameNotExistException() {
        super(MESSAGE);
    }
}
