package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class GameRegistration {

    private User host;

    private String gameName;

    private LocalDateTime matchStart;

    private SportsEvent sportsEvent;

    private Integer runningTime;

    @Builder
    public GameRegistration(User host, String gameName, LocalDateTime matchStart, SportsEvent sportsEvent, Integer runningTime) {
        this.host = host;
        this.gameName = gameName;
        this.matchStart = matchStart;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
    }
}
