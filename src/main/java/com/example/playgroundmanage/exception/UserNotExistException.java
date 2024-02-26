package com.example.playgroundmanage.exception;

public class UserNotExistException extends RuntimeException {
    private static final String MESSAGE = "不存在或无效的用户";
    public UserNotExistException() {
        super(MESSAGE);
    }
}
