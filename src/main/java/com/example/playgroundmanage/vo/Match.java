package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

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

    private Integer homeScore;

    private Integer awayScore;

    private LocalDate matchStart;

    private boolean isStarted;

    private boolean isFinished;
}
