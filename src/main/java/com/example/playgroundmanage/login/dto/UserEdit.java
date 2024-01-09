package com.example.playgroundmanage.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserEdit {
    private String userNickname;


    @Builder
    public UserEdit(String userNickname) {
        this.userNickname = userNickname;
    }
}
