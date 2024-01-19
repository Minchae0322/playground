package com.example.playgroundmanage.exception;

public class SchoolNotExistException extends RuntimeException{

    private static final String MESSAGE = "존재하지 않는 학교입니다.";


    public SchoolNotExistException() {
        super(MESSAGE);
    }
}
