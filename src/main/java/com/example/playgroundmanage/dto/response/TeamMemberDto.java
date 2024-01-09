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
    public TeamMemberDto(Long userId, String userNickname, InMemoryMultipartFile userProfileImg, String userRole) {
        this.userId = userId;
        this.userNickname = userNickname;
        if (userProfileImg != null) {
            try {
                this.userProfileImg = Base64.getEncoder().encodeToString(userProfileImg.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("이미지 변환 실패", e);
            }
        }
        this.userRole = userRole;
    }
}
