package com.example.playgroundmanage.exception;

public class SchoolNotExistException extends RuntimeException{

    private static final String MESSAGE = "不存在的学校";


    public SchoolNotExistException() {
        super(MESSAGE);
    }
}
