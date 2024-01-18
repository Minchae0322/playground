package com.example.playgroundmanage.exception;

public class PlaygroundNotExistException extends RuntimeException{
    private static final String MESSAGE = "운동장이 비활성화 되었거나, 존재하지 않습니다.";
    public PlaygroundNotExistException() {
        super(MESSAGE);
    }
}
