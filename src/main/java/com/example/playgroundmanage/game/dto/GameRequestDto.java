package com.example.playgroundmanage.game.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;
import lombok.*;


@Getter
@Setter
public class GameRequestDto extends RequestDto {

    private String requestType;

    private Long gameId;

    private Long subTeamId;

    private Long teamId;

    private GameTeamSide gameTeamSide;


    @Builder
    public GameRequestDto(User user, MyDateTime requestTime, String requestType, Long gameId, Long subTeamId, Long teamId, GameTeamSide gameTeamSide) {
        super(user, requestTime);
        this.requestType = requestType;
        this.gameId = gameId;
        this.subTeamId = subTeamId;
        this.teamId = teamId;
        this.gameTeamSide = gameTeamSide;
    }
}
