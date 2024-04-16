package com.example.playgroundmanage.login.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long userId;
    private String userNickname;

    private String userRole;

    private boolean isMine;

    private String userProfileImg;

    @Builder
    public UserResponseDto(Long userId, String userNickname, String userRole, boolean isMine, String userProfileImg) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userRole = userRole;
        this.isMine = isMine;
        this.userProfileImg = userProfileImg;
    }
}
