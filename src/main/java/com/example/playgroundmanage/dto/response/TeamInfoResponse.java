package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamInfoResponse {

    private Long teamId;
    private String teamName;

    private String teamImg;

    private String sportsEvent;

    private Long leaderId;

    private Long leaderName;

    @Builder
    public TeamInfoResponse(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
