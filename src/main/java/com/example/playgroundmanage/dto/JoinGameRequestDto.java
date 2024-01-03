package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JoinGameRequestDto {



    private Long subTeamId;

    private User user;

    private Long teamId;

    private MatchTeamSide matchTeamSide;

    private LocalDateTime requestTime;

    @Builder
    public JoinGameRequestDto( Long subTeamId, User user, Long teamId, MatchTeamSide matchTeamSide, LocalDateTime requestTime) {
        this.subTeamId = subTeamId;
        this.user = user;
        this.teamId = teamId;
        this.matchTeamSide = matchTeamSide;
        this.requestTime = requestTime;
    }
}
