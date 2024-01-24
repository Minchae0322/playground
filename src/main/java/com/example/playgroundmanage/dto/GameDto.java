package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
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

    private User host;

    private Playground playground;

    private String gameName;

    private Integer participantNum;

    private DateTime startDateTime;

    private SportsEvent sportsEvent;

    private Integer runningTime;

    private boolean isFriendly;

    @Builder
    public GameDto(Long gameId, User host, Integer participantNum, Playground playground, String gameName, DateTime startDateTime, SportsEvent sportsEvent, Integer runningTime, boolean isFriendly) {
        this.gameId = gameId;
        this.host = host;
        this.playground = playground;
        this.gameName = gameName;
        this.participantNum = participantNum;
        this.startDateTime = startDateTime;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
        this.isFriendly = isFriendly;
    }




    public GameThumbnail toGameThumbnail() {
        return GameThumbnail.builder()
                .gameStart(Util.localDateToYearMonthDateTimeString(startDateTime.getLocalDateTime()))
                .hostName(host.getNickname())
                .runningTime(runningTime)
                .gameName(gameName)

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
