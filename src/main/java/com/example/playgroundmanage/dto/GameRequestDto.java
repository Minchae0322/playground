package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
public class GameRequestDto extends RequestDto {

    private String requestType;

    private Long gameId;

    private Long subTeamId;

    private Long teamId;

    private MatchTeamSide matchTeamSide;


    @Builder
    public GameRequestDto(User user, MyDateTime requestTime, String requestType, Long gameId, Long subTeamId, Long teamId, MatchTeamSide matchTeamSide) {
        super(user, requestTime);
        this.requestType = requestType;
        this.gameId = gameId;
        this.subTeamId = subTeamId;
        this.teamId = teamId;
        this.matchTeamSide = matchTeamSide;
    }
}
