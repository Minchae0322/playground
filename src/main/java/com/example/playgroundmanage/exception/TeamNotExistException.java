package com.example.playgroundmanage.exception;

public class TeamNotExistException extends RuntimeException{
    private static final String MESSAGE = "팀이 존재 하지 않습니다.";
    public TeamNotExistException() {
        super(MESSAGE);
    }
}
