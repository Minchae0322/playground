package com.example.playgroundmanage.exception;

public class RequestNotExistException extends RuntimeException{
    private static final String MESSAGE = "요청이 존재하지 않습니다.";
    public RequestNotExistException() {
        super(MESSAGE);
    }
}
