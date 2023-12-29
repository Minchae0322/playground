package com.example.playgroundmanage.game.vo.impl;


import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.JoinGameRequest;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class TeamJoinGameRequest extends JoinGameRequest {

    @ManyToOne
    private Team team;

    public TeamJoinGameRequest(Long id, Game game, User user, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime, Team team) {
        super(id, game, user, matchTeamSide, expiredTime, requestTime);
        this.team = team;
    }

}
