package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SmallTeamRegistration {

    private Long matchTeamId;
    private Long teamId;
    private User user;

    private MatchTeamSide matchTeamSide;


    @Builder
    public SmallTeamRegistration(Long matchTeamId, Long teamId, User user, MatchTeamSide matchTeamSide) {
        this.matchTeamId = matchTeamId;
        this.teamId = teamId;
        this.user = user;
        this.matchTeamSide = matchTeamSide;
    }
}
