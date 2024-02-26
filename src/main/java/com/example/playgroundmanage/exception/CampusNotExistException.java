package com.example.playgroundmanage.exception;

public class CampusNotExistException extends RuntimeException {

    private static final String MESSAGE = "不存在的校区";
    public CampusNotExistException() {
        super(MESSAGE);
    }
}
