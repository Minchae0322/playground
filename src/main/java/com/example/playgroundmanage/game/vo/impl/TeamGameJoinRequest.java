package com.example.playgroundmanage.game.vo.impl;


import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.GameRequestInfoDto;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class TeamGameJoinRequest extends GameRequest {

    @ManyToOne
    private SubTeam subTeam;

    @Builder
    public TeamGameJoinRequest(Long id, Game game, User user, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime, SubTeam subTeam) {
        super(id, game, user, matchTeamSide, expiredTime, requestTime);
        this.subTeam = subTeam;
    }

    @Override
    public GameRequestInfoDto toGameRequestDto() {
        return GameRequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .requestType("teamGameJoin")
                .user(getUser())
                .subTeam(subTeam)
                .matchTeamSide(getMatchTeamSide())
                .requestTime(getRequestTime())
                .build();
    }
}
