package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SmallTeamRegistration {

    private Long matchId;
    private Team team;
    private User user;

    private MatchTeamSide matchTeamSide;


    @Builder
    public SmallTeamRegistration(Long matchId, Team team, User user, MatchTeamSide matchTeamSide) {
        this.matchId = matchId;
        this.team = team;
        this.user = user;
        this.matchTeamSide = matchTeamSide;
    }
}
