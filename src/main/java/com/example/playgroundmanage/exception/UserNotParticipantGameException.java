package com.example.playgroundmanage.exception;

public class UserNotParticipantGameException extends RuntimeException {
    private static final String MESSAGE = "게임에 참여하고 있지 않습니다.";
    public UserNotParticipantGameException() {
        super(MESSAGE);
    }
}
