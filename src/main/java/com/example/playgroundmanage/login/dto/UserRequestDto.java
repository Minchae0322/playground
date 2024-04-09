package com.example.playgroundmanage.login.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserRequestDto {
    private Long userId;
    private String userNickname;

    @Builder
    public UserRequestDto(Long userId, String userNickname) {
        this.userId = userId;
        this.userNickname = userNickname;
    }
}
