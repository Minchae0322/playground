package com.example.playgroundmanage.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class TeamRegistrationRequest {
    private String teamName;
    private String teamDescription;
    private String sportsEvent;

    @Builder
    public TeamRegistrationRequest(String teamName, String teamDescription, String sportsEvent) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.sportsEvent = sportsEvent;
    }
}
