package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GameRegistration {

    private User host;

    private String gameName;

    private MyDateTime myDateTime;

    private SportsEvent sportsEvent;

    private Integer runningTime;

    @Builder
    public GameRegistration(User host, String gameName, MyDateTime myDateTime, SportsEvent sportsEvent, Integer runningTime) {
        this.host = host;
        this.gameName = gameName;
        this.myDateTime = myDateTime;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
    }
}
