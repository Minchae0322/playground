package com.example.playgroundmanage.request.vo.impl;


import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SoloGameJoinRequest extends GameRequest {

    protected GameTeamSide gameTeamSide;

    @Builder
    public SoloGameJoinRequest(Long id, Game game, User user, User host, GameTeamSide gameTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime) {
        super(id, game, user, host, expiredTime, requestTime);
        this.gameTeamSide = gameTeamSide;
    }



    @Override
    public RequestInfoDto toGameRequestInfoDto() {
        return RequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .requestType("soloGameJoin")
                .user(getUser())
                .userProfileImg(getUser().getUserProfileImg().getFileUrl())
                .gameTeamSide(getGameTeamSide())
                .requestTime(getRequestTime())
                .build();
    }
}
