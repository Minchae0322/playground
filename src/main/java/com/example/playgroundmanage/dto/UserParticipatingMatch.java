package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class UserParticipatingMatch {
    private Long matchId;

    private User user;

    private MatchTeamSide matchTeamSide;

    @Builder
    public UserParticipatingMatch(Long matchId, User user, MatchTeamSide matchTeamSide) {
        this.matchId = matchId;
        this.user = user;
        this.matchTeamSide = matchTeamSide;
    }
}
