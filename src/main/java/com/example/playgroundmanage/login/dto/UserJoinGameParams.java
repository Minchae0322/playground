package com.example.playgroundmanage.login.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;


@Data
public class UserJoinGameParams {


    private Long subTeamId;

    private Long teamId;


    private String gameTeamSide;


    @Builder
    public UserJoinGameParams(Long subTeamId, Long teamId, String gameTeamSide) {
        this.subTeamId = subTeamId;
        this.teamId = teamId;
        this.gameTeamSide = gameTeamSide;
    }





    public GameRequestDto toJoinGameRequestDto(User user) {
        return GameRequestDto.builder()
                .user(user)
                .teamId(teamId)
                .subTeamId(subTeamId)
                .requestTime(MyDateTime.initMyDateTime(ZonedDateTime.now()))
                .gameTeamSide(GameTeamSide.valueOf(gameTeamSide))
                .build();
    }

}