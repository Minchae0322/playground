package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.vo.Playground;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ManyToOne(cascade = CascadeType.MERGE)
    private Playground playground;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User host;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoinGameRequest> joinGameRequests = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CompetingTeam homeTeam;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CompetingTeam awayTeam;

    @Enumerated
    private SportsEvent sportsEvent;

    private int homeScore;

    private int awayScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime gameStartDateTime;

    private Long runningTime;


    private boolean isStarted;

    private boolean isFinished;

    @Builder
    public Game(Long id, Playground playground, User host, List<JoinGameRequest> joinGameRequests, CompetingTeam homeTeam, CompetingTeam awayTeam, SportsEvent sportsEvent, int homeScore, int awayScore, LocalDateTime gameStartDateTime, Long runningTime, boolean isStarted, boolean isFinished) {
        this.id = id;
        this.playground = playground;
        this.host = host;
        this.joinGameRequests = joinGameRequests;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sportsEvent = sportsEvent;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
        this.isStarted = isStarted;
        this.isFinished = isFinished;
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

    public boolean gameOnGoing(LocalDateTime currentTime) {
        LocalDateTime gameEndDateTime = gameStartDateTime.plusMinutes(runningTime);
        return currentTime.isAfter(gameStartDateTime) && currentTime.isBefore(gameEndDateTime);
    }
}
