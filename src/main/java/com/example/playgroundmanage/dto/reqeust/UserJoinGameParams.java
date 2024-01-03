package com.example.playgroundmanage.dto.reqeust;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;


@Data
public class UserJoinGameParams {


    private Long subTeamId;

    private Long teamId;

    private String matchTeamSide;

    @Builder
    public UserJoinGameParams(Long subTeamId, Long teamId, String matchTeamSide) {
        this.subTeamId = subTeamId;
        this.teamId = teamId;
        this.matchTeamSide = matchTeamSide;
    }




    public JoinGameRequestDto toJoinGameRequestDto(User user) {
        return JoinGameRequestDto.builder()
                .user(user)
                .teamId(teamId)
                .subTeamId(subTeamId)
                .requestTime(MyDateTime.initMyDateTime(ZonedDateTime.now()).getLocalDateTime())
                .matchTeamSide(MatchTeamSide.valueOf(matchTeamSide))
                .build();
    }

}
