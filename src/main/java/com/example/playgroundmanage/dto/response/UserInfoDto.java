package com.example.playgroundmanage.dto.response;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserInfoDto {
    private Long userId;
    private String userNickname;

    @Builder
    public UserInfoDto(Long userId, String userNickname) {
        this.userId = userId;
        this.userNickname = userNickname;
    }
}
