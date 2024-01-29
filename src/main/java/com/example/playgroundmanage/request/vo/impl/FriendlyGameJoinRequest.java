package com.example.playgroundmanage.request.vo.impl;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FriendlyGameJoinRequest extends GameRequest {


    @Builder
    public FriendlyGameJoinRequest(Long id, Game game, User user, User host, LocalDateTime expiredTime, LocalDateTime requestTime) {
        super(id, game, user, host, expiredTime, requestTime);
    }

    @Override
    public RequestInfoDto toGameRequestInfoDto() {
        return RequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .requestType("FriendlyGameJoin")
                .user(getUser())
                .requestTime(getRequestTime())
                .build();
    }

}
