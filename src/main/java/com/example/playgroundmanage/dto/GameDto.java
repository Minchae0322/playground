package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.util.Util;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GameDto {

    private Long gameId;

    private Long playgroundId;

    private User host;

    private GameType gameType;

    private Playground playground;

    private String gameName;

    private Integer participantNum;

    private DateTime startDateTime;

    private SportsEvent sportsEvent;

    private Integer runningTime;



    @Builder
    public GameDto(Long gameId, Long playgroundId, User host, GameType gameType, Playground playground, String gameName, Integer participantNum, DateTime startDateTime, SportsEvent sportsEvent, Integer runningTime) {
        this.gameId = gameId;
        this.playgroundId = playgroundId;
        this.host = host;
        this.gameType = gameType;
        this.playground = playground;
        this.gameName = gameName;
        this.participantNum = participantNum;
        this.startDateTime = startDateTime;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
    }

    public GameThumbnail toGameThumbnail() {
        return GameThumbnail.builder()
                .gameStart(Util.localDateToYearMonthDateTimeString(startDateTime.getLocalDateTime()))
                .hostName(host.getNickname())
                .runningTime(runningTime)
                .gameName(gameName)
                .gameType(gameType.getValue())
                .playgroundId(playground.getId())
                .playgroundName(playground.getName())
                .campusName(playground.getCampus().getCampusName())
                .sportsEvent(sportsEvent)
                .gameId(gameId)
                .build();
    }

    public GameThumbnail toGameThumbnail(InMemoryMultipartFile hostProfileImg) {
        return GameThumbnail.builder()
                .gameStart(Util.localDateToYearMonthDateTimeString(startDateTime.getLocalDateTime()))
                .hostName(host.getNickname())
                .runningTime(runningTime)
                .gameName(gameName)
                .playgroundId(playground.getId())
                .gameType(gameType.getValue())
                .participantNum(participantNum)
                .gameStartDateTime(startDateTime.getLocalDateTime())
                .playgroundName(playground.getName())
                .hostProfileImg(hostProfileImg)
                .sportsEvent(sportsEvent)
                .gameId(gameId)
                .build();
    }

    public GameTimeDto toGameDateDto() {
        return GameTimeDto.builder()
                .runningTime(runningTime)
                .startDateTime(startDateTime)
                .build();
    }


}
