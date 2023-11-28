package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class MatchRegistration {

    private User host;

    private LocalDate matchStart;

    private SportsEvent sportsEvent;

    private Long runningTime;

    @Builder
    public MatchRegistration(User host, LocalDate matchStart, SportsEvent sportsEvent, Long runningTime) {
        this.host = host;
        this.matchStart = matchStart;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
    }
}
