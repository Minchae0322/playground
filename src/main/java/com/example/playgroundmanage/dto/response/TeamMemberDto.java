package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Base64;


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
