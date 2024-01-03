package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.Util;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GameThumbnail {

    private Long gameId;

    private String gameName;

    private String gameStart;

    private Integer runningTime;

    private SportsEvent sportsEvent;

    private String hostName;

    @Builder
    public GameThumbnail(Long gameId, String gameName, String gameStart, Integer runningTime, SportsEvent sportsEvent, String hostName) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameStart = gameStart;
        this.runningTime = runningTime;
        this.sportsEvent = sportsEvent;
        this.hostName = hostName;
    }
}
