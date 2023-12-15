package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamInfoResponse {

    private String teamName;

    @Builder
    public TeamInfoResponse(String teamName) {
        this.teamName = teamName;
    }
}
