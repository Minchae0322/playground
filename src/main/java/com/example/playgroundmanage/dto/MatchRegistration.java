package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class MatchRegistration {

    private User host;

    private String gameName;

    private LocalDateTime matchStart;

    private SportsEvent sportsEvent;

    private Long runningTime;

    @Builder
    public MatchRegistration(User host, String gameName, LocalDateTime matchStart, SportsEvent sportsEvent, Long runningTime) {
        this.host = host;
        this.gameName = gameName;
        this.matchStart = matchStart;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
    }
}
