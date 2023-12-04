package com.example.playgroundmanage.vo;

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

    private LocalDateTime expiredTime;

    private LocalDateTime requestTime;

    @Builder
    public JoinGameRequest(Long id, boolean isSoloTeam, Game game, Team team, User user, LocalDateTime expiredTime) {
        this.id = id;
        this.isSoloTeam = isSoloTeam;
        this.game = game;
        this.team = team;
        this.user = user;
        this.expiredTime = expiredTime;
        this.requestTime = LocalDateTime.now();
    }

    public JoinGameRequest update(Team team) {
        this.team = team;
        this.requestTime = LocalDateTime.now();
        return this;
    }

    public JoinGameRequest update() {
        this.requestTime = LocalDateTime.now();
        return this;
    }
}
