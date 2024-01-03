package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameRequestDto {

    private String requestType;

    private Long subTeamId;

    private User user;

    private Long teamId;

    private MatchTeamSide matchTeamSide;

    private LocalDateTime requestTime;
    //todo gameRequestDto 를 분리해야 하는데 subTeamId 와 teamId 가 null 일 수 있다.
    @Builder
    public GameRequestDto(String requestType, Long subTeamId, User user, Long teamId, MatchTeamSide matchTeamSide, LocalDateTime requestTime) {
        this.requestType = requestType;
        this.subTeamId = subTeamId;
        this.user = user;
        this.teamId = teamId;
        this.matchTeamSide = matchTeamSide;
        this.requestTime = requestTime;
    }
}
