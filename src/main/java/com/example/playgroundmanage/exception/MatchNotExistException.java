package com.example.playgroundmanage.exception;

public class MatchNotExistException  extends RuntimeException{
    private static final String MESSAGE = "比赛不存在";
    public MatchNotExistException() {
        super(MESSAGE);
    }
}
