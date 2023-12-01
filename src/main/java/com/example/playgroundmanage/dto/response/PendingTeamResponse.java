package com.example.playgroundmanage.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public class PendingTeamResponse {
    private String teamName;

    private Long userId;

    private String userName;

    @Builder
    public PendingTeamResponse(String teamName, Long userId, String userName) {
        this.teamName = teamName;
        this.userId = userId;
        this.userName = userName;
    }
}
