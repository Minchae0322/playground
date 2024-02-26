package com.example.playgroundmanage.exception;

public class NoOngoingGameException extends RuntimeException {
    private static final String MESSAGE = "没有正在进行的比赛";
    public NoOngoingGameException() {
        super(MESSAGE);
    }
}
