package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import lombok.*;

import java.io.IOException;
import java.util.Base64;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamInfoResponse {

    private Long teamId;
    private String teamName;

    private String teamProfileImg;

    private String sportsEvent;

    private String description;

    private Long leaderId;

    private String leaderName;

    @Builder
    public TeamInfoResponse(Long teamId, String teamName, InMemoryMultipartFile teamProfileImg, String sportsEvent, Long leaderId, String leaderName, String description) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamProfileImg = FileHandlerImpl.multipartFileToString(teamProfileImg);
        this.sportsEvent = sportsEvent;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.description = description;
    }
}
