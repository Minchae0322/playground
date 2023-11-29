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
    public Match(User host, MatchTeam homeTeam, MatchTeam awayTeam, SportsEvent sportsEvent, LocalDate matchStart, Long runningTime) {
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

    public MatchTeam getMatchTeamBySide(MatchTeamSide matchTeamSide) {
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
