package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class UserParticipatingMatch {
    private Long matchId;

    private User user;

    private GameTeamSide gameTeamSide;

    @Builder
    public UserParticipatingMatch(Long matchId, User user, GameTeamSide gameTeamSide) {
        this.matchId = matchId;
        this.user = user;
        this.gameTeamSide = gameTeamSide;
    }
}
