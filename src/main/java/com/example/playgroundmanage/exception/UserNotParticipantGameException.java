package com.example.playgroundmanage.exception;

public class UserNotParticipantGameException extends RuntimeException {
    private static final String MESSAGE = "用户现在不参与比赛";
    public UserNotParticipantGameException() {
        super(MESSAGE);
    }
}
