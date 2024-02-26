package com.example.playgroundmanage.exception;

public class RequestNotExistException extends RuntimeException{
    private static final String MESSAGE = "请求不存在";
    public RequestNotExistException() {
        super(MESSAGE);
    }
}
