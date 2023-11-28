package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.dto.MatchTeamRegistration;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity(name = "matchUp")
@Getter
@RequiredArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User host;

    @OneToOne(cascade = CascadeType.ALL)
    private MatchTeam homeTeam;

    @OneToOne(cascade = CascadeType.ALL)
    private MatchTeam awayTeam;

    @Enumerated
    private SportsEvent sportsEvent;

    private int homeScore;

    private int awayScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate matchStart;

    private Long runningTime;

    private boolean isStarted;

    private boolean isFinished;


    @Builder

    public Match(Long id, User host, MatchTeam homeTeam, MatchTeam awayTeam, SportsEvent sportsEvent, int homeScore, int awayScore, LocalDate matchStart, Long runningTime, boolean isStarted, boolean isFinished) {
        this.id = id;
        this.host = host;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sportsEvent = sportsEvent;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchStart = matchStart;
        this.runningTime = runningTime;
        this.isStarted = isStarted;
        this.isFinished = isFinished;
    }
}
