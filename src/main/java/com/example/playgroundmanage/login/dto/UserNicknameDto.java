package com.example.playgroundmanage.login.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserNicknameDto {
    private String userNickname;


    @Builder
    public UserNicknameDto(String userNickname) {
        this.userNickname = userNickname;
    }
}
