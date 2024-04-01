package com.example.playgroundmanage.login.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserRecordResponse {

    private String userNickname;

    private String userProfileImg;

    private int ranking;

    private int win;
    private int draw;
    private int lose;

    @Builder
    public UserRecordResponse(String userNickname, String userProfileImg, int ranking, int win, int draw, int lose) {
        this.userNickname = userNickname;
        this.userProfileImg = userProfileImg;
        this.ranking = ranking;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }
}
