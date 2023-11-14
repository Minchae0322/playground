package com.example.playgroundmanage.exception;

public class ExistUserException extends RuntimeException{

    private static final String MESSAGE = "이미 등록된 사용자 입니다.";
    public ExistUserException() {
        super(MESSAGE);
    }
}
