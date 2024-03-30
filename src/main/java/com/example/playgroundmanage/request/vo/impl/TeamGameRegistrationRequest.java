package com.example.playgroundmanage.request.vo.impl;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamGameRegistrationRequest extends GameRequest {

    @ManyToOne
    private Team team;

    private GameTeamSide gameTeamSide;

    @Builder
    public TeamGameRegistrationRequest(Long id, Game game, User user, User host, GameTeamSide gameTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime, Team team) {
        super(id, game, user, host, expiredTime, requestTime);
        this.team = team;
        this.gameTeamSide = gameTeamSide;
    }




    @Override
    public RequestInfoDto toGameRequestInfoDto() {
        return RequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .userProfileImg(getUser().getProfileImg().getFileUrl())
                .requestType("teamGameRegistration")
                .user(getUser())
                .team(team)
                .gameTeamSide(gameTeamSide)
                .requestTime(getRequestTime())
                .build();
    }
}
