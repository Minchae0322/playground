package com.example.playgroundmanage.login.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserRecordResponse {

    private int win;
    private int draw;
    private int lose;

    @Builder
    public UserRecordResponse(int win, int draw, int lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }
}
