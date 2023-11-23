package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private MatchTeam homeTeam;

    @OneToOne
    private MatchTeam awayTeam;

    @OneToOne
    private MatchResult matchResult;

    private boolean isStarted;

    private boolean isFinished;
}
