package com.example.playgroundmanage.exception;

public class NoOngoingGameException extends RuntimeException {
    private static final String MESSAGE = "진행중인 경기가 없습니다.";
    public NoOngoingGameException() {
        super(MESSAGE);
    }
}
