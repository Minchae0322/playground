package com.example.playgroundmanage.request.vo.impl;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
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

    protected GameTeamSide gameTeamSide;

    @Builder
    public FriendlyGameJoinRequest(Long id, Game game, User user, User host, LocalDateTime expiredTime, LocalDateTime requestTime, GameTeamSide gameTeamSide) {
        super(id, game, user, host, expiredTime, requestTime);
        this.gameTeamSide = gameTeamSide;
    }

    @Override
    public RequestInfoDto toGameRequestInfoDto() {
        return RequestInfoDto.builder()
                .game(getGame())
                .requestId(getId())
                .gameTeamSide(gameTeamSide)
                .requestType("friendlyGameJoin")
                .user(getUser())
                .userProfileImg(getUser().getProfileImg().getFileUrl())
                .requestTime(getRequestTime())
                .build();
    }

}
