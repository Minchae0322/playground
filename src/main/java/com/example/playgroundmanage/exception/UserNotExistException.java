package com.example.playgroundmanage.exception;

public class UserNotExistException extends RuntimeException {
    private static final String MESSAGE = "없는 사용자거나 잘못된 사용자입니다.";
    public UserNotExistException() {
        super(MESSAGE);
    }
}
