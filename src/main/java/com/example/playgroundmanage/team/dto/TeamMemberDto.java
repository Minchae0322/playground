package com.example.playgroundmanage.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class TeamMemberDto {
    private Long userId;
    private String userNickname;

    private String userProfileImg;

    private String userRole;

    @Builder
    public TeamMemberDto(Long userId, String userNickname, String userProfileImg, String userRole) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImg = userProfileImg;
        this.userRole = userRole;
    }
}
