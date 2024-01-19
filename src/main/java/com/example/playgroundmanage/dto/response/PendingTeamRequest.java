package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PendingTeamRequest {
    private Long requestId;

    private String introduction;

    private String teamName;

    private String userName;

    private Long userId;

    private LocalDateTime requestTime;

    @Builder
    public PendingTeamRequest(Long requestId, String introduction, String teamName, String userName, Long userId, LocalDateTime requestTime) {
        this.requestId = requestId;
        this.introduction = introduction;
        this.teamName = teamName;
        this.userName = userName;
        this.userId = userId;
        this.requestTime = requestTime;
    }
}
