package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class JoinGameRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isSoloTeam;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Team team;

    @ManyToOne
    private User user;

    private MatchTeamSide matchTeamSide;

    private LocalDateTime expiredTime;

    private LocalDateTime requestTime;

    @Builder
    public JoinGameRequest(Long id, boolean isSoloTeam, Game game, Team team, User user, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime) {
        this.id = id;
        this.isSoloTeam = isSoloTeam;
        this.game = game;
        this.team = team;
        this.user = user;
        this.matchTeamSide = matchTeamSide;
        this.expiredTime = expiredTime;
        this.requestTime = requestTime;
    }




    public JoinGameRequest update(Team team, MatchTeamSide matchTeamSide) {
        this.team = team;
        this.matchTeamSide = matchTeamSide;
        this.requestTime = LocalDateTime.now();
        return this;
    }

    public JoinGameRequest update(MatchTeamSide matchTeamSide) {
        this.requestTime = LocalDateTime.now();
        this.matchTeamSide = matchTeamSide;
        return this;
    }

    public String getTeamNameOrSoloTeam() {
        if (team == null) {
            return "SOLO";
        }
        return team.getTeamName();
    }
}
