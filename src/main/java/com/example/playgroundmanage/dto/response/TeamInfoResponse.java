package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Base64;

@Getter
@RequiredArgsConstructor
public class TeamInfoResponse {

    private Long teamId;
    private String teamName;

    private String teamProfileImg;

    private String sportsEvent;

    private Long leaderId;

    private String leaderName;

    @Builder
    public TeamInfoResponse(Long teamId, String teamName, InMemoryMultipartFile teamProfileImg, String sportsEvent, Long leaderId, String leaderName) {
        this.teamId = teamId;
        this.teamName = teamName;
        if (teamProfileImg != null) {
            try {
                this.teamProfileImg = Base64.getEncoder().encodeToString(teamProfileImg.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("이미지 변환 실패", e);
            }
        }
        this.sportsEvent = sportsEvent;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
    }
}
