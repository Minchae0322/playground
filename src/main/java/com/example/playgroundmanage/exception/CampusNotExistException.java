package com.example.playgroundmanage.exception;

public class CampusNotExistException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 캠퍼스 입니다.";
    public CampusNotExistException() {
        super(MESSAGE);
    }
}
