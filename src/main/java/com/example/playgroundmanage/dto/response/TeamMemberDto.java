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

    private String role;

    @Builder
    public TeamMemberDto(Long userId, String userNickname, InMemoryMultipartFile userProfileImg, String role) {
        this.userId = userId;
        this.userNickname = userNickname;
        try {
            this.userProfileImg = Base64.getEncoder().encodeToString(userProfileImg.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 변환 실패", e);
        }
        this.role = role;
    }
}
