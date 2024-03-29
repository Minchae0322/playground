package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.type.SportsEvent;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;



@Data
@RequiredArgsConstructor
public class GameThumbnail {

    private Long gameId;

    private String gameName;

    private String gameStart;

    private Integer participantNum;

    private LocalDateTime gameStartDateTime;

    private Integer runningTime;

    private SportsEvent sportsEvent;

    private String campusName;

    private String playgroundName;

    private Long playgroundId;

    private String hostProfileImg;

    private String gameType;

    private String hostName;

    @Builder
    public GameThumbnail(Long gameId, String gameName, Long playgroundId, String gameType, Integer participantNum, LocalDateTime gameStartDateTime , String gameStart, String playgroundName, String campusName, Integer runningTime, SportsEvent sportsEvent, String hostProfileImg, String hostName) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameStart = gameStart;
        this.runningTime = runningTime;
        this.gameType = gameType;
        this.sportsEvent = sportsEvent;
        this.campusName = campusName;
        this.playgroundId = playgroundId;
        this.participantNum = participantNum;
        this.gameStartDateTime = gameStartDateTime;
        this.playgroundName = playgroundName;
        this.hostProfileImg = hostProfileImg;
        this.hostName = hostName;
    }
}
