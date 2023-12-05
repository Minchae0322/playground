package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamRegistration {

    private String teamName;

    private String teamPic;

    private User leader;

    private SportsEvent sportsEvent;

    @Builder
    public TeamRegistration(String teamName, String teamPic, User leader, SportsEvent sportsEvent) {
        this.teamName = teamName;
        this.teamPic = teamPic;
        this.leader = leader;
        this.sportsEvent = sportsEvent;
    }
}
