package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubTeamRegistrationParams {

    private Long gameId;
    private Long teamId;

    private GameTeamSide gameTeamSide;
    private User user;

    @Builder
    public SubTeamRegistrationParams(Long gameId, Long teamId, GameTeamSide gameTeamSide, User user) {
        this.gameId = gameId;
        this.teamId = teamId;
        this.gameTeamSide = gameTeamSide;
        this.user = user;
    }

}
