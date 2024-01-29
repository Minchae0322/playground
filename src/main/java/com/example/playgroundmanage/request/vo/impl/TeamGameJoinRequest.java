package com.example.playgroundmanage.request.vo.impl;


import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamGameJoinRequest extends GameRequest {

    @ManyToOne
    private SubTeam subTeam;

    private GameTeamSide gameTeamSide;


    @Builder
    public TeamGameJoinRequest(Long id, Game game, User user, User host, GameTeamSide gameTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime, SubTeam subTeam) {
        super(id, game, user, host,  expiredTime, requestTime);
        this.subTeam = subTeam;
        this.gameTeamSide = gameTeamSide;
    }

    @Override
    public RequestInfoDto toGameRequestInfoDto() {
        return RequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .requestType("teamGameJoin")
                .user(getUser())
                .subTeam(subTeam)
                .gameTeamSide(gameTeamSide)
                .requestTime(getRequestTime())
                .build();
    }
}
