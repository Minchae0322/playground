package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import lombok.Builder;
import lombok.Data;

@Data
public class TeamResponseDto {
    private Long teamId;

    private String teamProfileImg;

    private String teamName;

    private String teamDescription;

    private String sportsEvent;

    @Builder
    public TeamResponseDto(Long teamId, InMemoryMultipartFile teamProfileImg, String teamName, String teamDescription, String sportsEvent) {
        this.teamId = teamId;
        this.teamProfileImg = FileHandlerImpl.multipartFileToString(teamProfileImg);
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.sportsEvent = sportsEvent;
    }
}
