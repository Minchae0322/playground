package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class MatchTeamRegistration {

    private Long matchId;
    private Team team;
    private User user;


    @Builder
    public MatchTeamRegistration(Long matchId, Team team, User users) {
        this.matchId = matchId;
        this.team = team;
        this.user = users;
    }
}
