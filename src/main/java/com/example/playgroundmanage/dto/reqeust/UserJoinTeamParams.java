package com.example.playgroundmanage.dto.reqeust;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.TeamJoinRequestDto;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UserJoinTeamParams {

    private String introduction;

    private Long teamId;


    @Builder
    public UserJoinTeamParams(String introduction, Long teamId) {
        this.introduction = introduction;
        this.teamId = teamId;
    }




    public TeamJoinRequestDto toTeamRequestDto(User user) {
        return TeamJoinRequestDto.builder()
                .user(user)
                .introduction(introduction)
                .requestTime(MyDateTime.initMyDateTime(ZonedDateTime.now()))
                .teamId(teamId)
                .build();
    }
}
