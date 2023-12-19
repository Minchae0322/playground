package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserProfileDto {
    private Long userId;
    private String userNickname;

    @Builder
    public UserProfileDto(Long userId, String userNickname) {
        this.userId = userId;
        this.userNickname = userNickname;
    }
}
