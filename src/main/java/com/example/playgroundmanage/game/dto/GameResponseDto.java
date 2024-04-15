package com.example.playgroundmanage.game.dto;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.Builder;
import lombok.Data;

@Data
public class GameResponseDto {

    private Long gameId;

    private Long playgroundId;

    private String hostName;

    private String hostProfileImg;

    private String gameType;

    private String playgroundName;

    private String gameName;

    private Integer participantNum;

    private String gameStartDateTime;

    private String sportsEvent;

    private Integer runningTime;

    private String campusName;

    @Builder
    public GameResponseDto(Long gameId, Long playgroundId, String hostName, String hostProfileImg, String gameType, String playgroundName, String gameName, Integer participantNum, String gameStartDateTime, String sportsEvent, Integer runningTime, String campusName) {
        this.gameId = gameId;
        this.playgroundId = playgroundId;
        this.hostName = hostName;
        this.hostProfileImg = hostProfileImg;
        this.gameType = gameType;
        this.playgroundName = playgroundName;
        this.gameName = gameName;
        this.participantNum = participantNum;
        this.gameStartDateTime = gameStartDateTime;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
        this.campusName = campusName;
    }
}
