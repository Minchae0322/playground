package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubTeamRegistrationParams {

    private Long gameId;
    private Long teamId;

    private MatchTeamSide matchTeamSide;
    private User user;

    @Builder
    public SubTeamRegistrationParams(Long gameId, Long teamId, MatchTeamSide matchTeamSide, User user) {
        this.gameId = gameId;
        this.teamId = teamId;
        this.matchTeamSide = matchTeamSide;
        this.user = user;
    }

    public void addUser(User user) {
        this.user = user;
    }
}
