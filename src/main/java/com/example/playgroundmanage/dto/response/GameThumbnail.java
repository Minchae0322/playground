package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.Util;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GameThumbnail {

    private Long gameId;

    private String gameStart;

    private Integer time;

    private SportsEvent sportsEvent;

    private String hostName;

    @Builder
    public GameThumbnail(Long gameId, String gameStart, Integer time, SportsEvent sportsEvent, String hostName) {
        this.gameId = gameId;
        this.gameStart = gameStart;
        this.time = time;
        this.sportsEvent = sportsEvent;
        this.hostName = hostName;
    }

    public static GameThumbnail GameToGameThumbnail(Game game) {
        return GameThumbnail.builder()
                .gameId(game.getId())
                .gameStart(Util.localDateToYearMonthDateTimeString(game.getGameStartDateTime()))
                .hostName(game.getHost().getNickname())
                .time(game.getRunningTime())
                .sportsEvent(game.getSportsEvent())
                .build();
    }
}
