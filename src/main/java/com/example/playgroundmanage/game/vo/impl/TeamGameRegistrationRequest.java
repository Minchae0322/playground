package com.example.playgroundmanage.game.vo.impl;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.type.MatchTeamSide;
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

    @Builder
    public TeamGameRegistrationRequest(Long id, Game game, User user, User host, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime, Team team) {
        super(id, game, user, host, matchTeamSide, expiredTime, requestTime);
        this.team = team;
    }




    @Override
    public RequestInfoDto toGameRequestInfoDto() {
        return RequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .requestType("teamGameRegistration")
                .user(getUser())
                .team(team)
                .matchTeamSide(getMatchTeamSide())
                .requestTime(getRequestTime())
                .build();
    }
}
