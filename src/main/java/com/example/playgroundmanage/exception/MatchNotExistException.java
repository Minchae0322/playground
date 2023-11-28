package com.example.playgroundmanage.exception;

public class MatchNotExistException  extends RuntimeException{
    private static final String MESSAGE = "경기가 존재하지 않습니다.";
    public MatchNotExistException() {
        super(MESSAGE);
    }
}
