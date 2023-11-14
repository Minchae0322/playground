package com.example.playgroundmanage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserEdit {
    private String username;


    @Builder
    public UserEdit(String username) {
        this.username = username;
    }
}
