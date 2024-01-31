package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class PendingGameRequest {
    private Long requestId;

    private Long gameId;

    private String gameName;

    private Long teamId;

    private String teamName;

    private String subTeamName;

    private String gameTeamSide;

    private Long userId;

    private String username;

    private String requestTime;

    private String requestType;

    @Builder
    public PendingGameRequest(Long requestId, Long gameId, String gameName, Long teamId, String teamName, String subTeamName, String gameTeamSide, Long userId, String username, String requestTime, String requestType) {
        this.requestId = requestId;
        this.gameId = gameId;
        this.gameName = gameName;
        this.teamId = teamId;
        this.teamName = teamName;
        this.subTeamName = subTeamName;
        this.gameTeamSide = gameTeamSide;
        this.userId = userId;
        this.username = username;
        this.requestTime = requestTime;
        this.requestType = requestType;
    }
}
