package com.example.playgroundmanage.exception;

public class TimeOverlappingException extends RuntimeException {

    private static final String MESSAGE = "지정한 시간에 이미 다른게임이 존재합니다.";
    public TimeOverlappingException(String time) {
        super(MESSAGE + time);
    }
}
