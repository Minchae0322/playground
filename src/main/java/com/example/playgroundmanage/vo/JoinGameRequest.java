package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
public class JoinGameRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isSoloTeam;

    @ManyToOne
    private Team team;

    @ManyToOne
    private User teamMaker;

    private LocalDateTime expiredTime;

    private LocalDateTime requestTime;

    @Builder
    public JoinGameRequest(Long id, boolean isSoloTeam, Team team, User teamMaker, LocalDateTime expiredTime, LocalDateTime requestTime) {
        this.id = id;
        this.isSoloTeam = isSoloTeam;
        this.team = team;
        this.teamMaker = teamMaker;
        this.expiredTime = expiredTime;
        this.requestTime = requestTime;
    }
}
