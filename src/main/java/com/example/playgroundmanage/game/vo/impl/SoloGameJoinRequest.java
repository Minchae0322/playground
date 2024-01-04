package com.example.playgroundmanage.game.vo.impl;


import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.GameRequestInfoDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameRequest;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SoloGameJoinRequest extends GameRequest {

    @Builder
    public SoloGameJoinRequest(Long id, Game game, User user, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime) {
        super(id, game, user, matchTeamSide, expiredTime, requestTime);
    }

    @Override
    public GameRequestInfoDto toGameRequestDto() {
        return GameRequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .requestType("soloGameJoin")
                .user(getUser())
                .matchTeamSide(getMatchTeamSide())
                .requestTime(getRequestTime())
                .build();
    }
}
