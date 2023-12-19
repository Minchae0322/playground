package com.example.playgroundmanage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class TeamRegistrationRequest {
    private String teamName;
    private String teamDescription;

    private MultipartFile teamPic;

    @Builder
    public TeamRegistrationRequest(String teamName, String teamDescription, MultipartFile teamPic) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.teamPic = teamPic;
    }
}
