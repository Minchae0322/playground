package com.example.playgroundmanage.althlectis.vo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class AthleticsResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int homeScore;

    private int awayScore;

    @OneToOne
    private Athletics athletics;


    boolean isFinished;
    @Builder
    public AthleticsResult(Long id, int homeScore, int awayScore, Athletics athletics) {
        this.id = id;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.athletics = athletics;
        this.isFinished = false;
    }

    public AthleticsResult update(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.isFinished = true;
        return this;
    }
}
