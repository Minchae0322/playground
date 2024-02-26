package com.example.playgroundmanage.exception;

public class TimeOverlappingException extends RuntimeException {

    private static final String MESSAGE = "在指定的时间已经存在其他比赛了";
    public TimeOverlappingException(String time) {
        super(MESSAGE + time);
    }
}
