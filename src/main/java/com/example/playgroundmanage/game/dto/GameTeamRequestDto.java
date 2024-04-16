package com.example.playgroundmanage.game.dto;

import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;
import lombok.Builder;
import lombok.Data;

@Data
public class GameTeamRequestDto {
    private Long gameId;
    private GameTeamSide gameTeamSide;
    private User user;

    @Builder
    public GameTeamRequestDto(Long gameId, GameTeamSide gameTeamSide, User user) {
        this.gameId = gameId;
        this.gameTeamSide = gameTeamSide;
        this.user = user;
    }
}
