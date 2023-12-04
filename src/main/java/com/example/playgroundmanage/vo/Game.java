package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User host;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CompetingTeam homeTeam;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CompetingTeam awayTeam;

    @Enumerated
    private SportsEvent sportsEvent;

    private int homeScore;

    private int awayScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime matchStart;

    private Long runningTime;

    private boolean isStarted;

    private boolean isFinished;


    @Builder
    public Game(User host, CompetingTeam homeTeam, CompetingTeam awayTeam, SportsEvent sportsEvent, LocalDateTime matchStart, Long runningTime) {
        validate(host, runningTime);
        this.host = host;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sportsEvent = sportsEvent;
        this.matchStart = matchStart;
        this.runningTime = runningTime;
        this.homeScore = 0;
        this.awayScore = 0;
        this.isStarted = false;
        this.isFinished = false;

    }

    public CompetingTeam getMatchTeamBySide(MatchTeamSide matchTeamSide) {
        if(matchTeamSide == MatchTeamSide.HOME) {
            return this.homeTeam;
        }
        return this.awayTeam;
    }

    private void validate(User host, Long runningTime) {
        if(host == null) {
            throw new UserNotExistException();
        }
        if(runningTime < 0 || runningTime > 120) {
            throw new IllegalArgumentException("경기 소요 시간이 올바르지 않습니다.");
        }
    }
}
