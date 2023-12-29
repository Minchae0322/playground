package com.example.playgroundmanage.game.vo.impl;


import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.JoinGameRequest;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@Getter
public class SoloJoinGameRequest extends JoinGameRequest {

    @Builder
    public SoloJoinGameRequest(Long id, Game game, User user, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime) {
        super(id, game, user, matchTeamSide, expiredTime, requestTime);
    }
}
