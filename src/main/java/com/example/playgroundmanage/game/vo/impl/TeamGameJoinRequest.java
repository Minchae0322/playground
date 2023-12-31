package com.example.playgroundmanage.game.vo.impl;


import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class TeamGameJoinRequest extends GameJoinRequest {

    @ManyToOne
    private SubTeam subTeam;

    @Builder
    public TeamGameJoinRequest(Long id, Game game, User user, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime, SubTeam team) {
        super(id, game, user, matchTeamSide, expiredTime, requestTime);
        this.subTeam = team;
    }

}
