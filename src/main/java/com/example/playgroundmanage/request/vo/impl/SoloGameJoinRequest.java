package com.example.playgroundmanage.request.vo.impl;


import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SoloGameJoinRequest extends GameRequest {


    @Builder
    public SoloGameJoinRequest(Long id, Game game, User user, User host, GameTeamSide gameTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime) {
        super(id, game, user, host, gameTeamSide, expiredTime, requestTime);
    }



    @Override
    public RequestInfoDto toGameRequestInfoDto() {
        return RequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .requestType("soloGameJoin")
                .user(getUser())
                .gameTeamSide(getGameTeamSide())
                .requestTime(getRequestTime())
                .build();
    }
}
