package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PendingJoinRequest {
    private Long requestId;

    private Long gameId;

    private String teamName;

    private String gameInfo;

    private String gameSide;

    private String username;

    private LocalDateTime requestTime;

    private boolean isSoloTeam;

    @Builder
    public PendingJoinRequest(Long requestId, Long gameId, String teamName, String gameInfo, String gameSide, String username, LocalDateTime requestTime, boolean isSoloTeam) {
        this.requestId = requestId;
        this.gameId = gameId;
        this.teamName = teamName;
        this.gameInfo = gameInfo;
        this.gameSide = gameSide;
        this.username = username;
        this.requestTime = requestTime;
        this.isSoloTeam = isSoloTeam;
        setSoloTeam(isSoloTeam, teamName);
    }




    private void setSoloTeam(boolean isSoloTeam, String teamName) {
        if (isSoloTeam || teamName == null) {
            this.teamName = "SOLO";
        }
    }

}
