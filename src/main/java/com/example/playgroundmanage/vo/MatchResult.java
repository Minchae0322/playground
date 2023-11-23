package com.example.playgroundmanage.vo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Match match;

    @OneToOne(cascade = CascadeType.PERSIST)
    private TeamMatchInfo homeTeam;

    @OneToOne(cascade = CascadeType.PERSIST)
    private TeamMatchInfo awayTeam;

    private LocalDate matchStart;



}
