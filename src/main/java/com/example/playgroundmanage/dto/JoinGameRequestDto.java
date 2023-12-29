package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JoinGameRequestDto {

    private Long gameId;

    private User user;

    private Long teamId;

    private MatchTeamSide matchTeamSide;

    private LocalDateTime requestTime;

    @Builder
    public JoinGameRequestDto(Long gameId, User user, Long teamId, MatchTeamSide matchTeamSide, LocalDateTime requestTime) {
        this.gameId = gameId;
        this.user = user;
        this.teamId = teamId;
        this.matchTeamSide = matchTeamSide;
        this.requestTime = requestTime;
    }
}
