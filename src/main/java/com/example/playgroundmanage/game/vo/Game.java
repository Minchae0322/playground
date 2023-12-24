package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.vo.Playground;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameName;

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
    public Game(Long id, String gameName, Playground playground, User host, List<JoinGameRequest> joinGameRequests, CompetingTeam homeTeam, CompetingTeam awayTeam, SportsEvent sportsEvent, int homeScore, int awayScore, LocalDateTime gameStartDateTime, Long runningTime, boolean isStarted, boolean isFinished) {
        validate(host, runningTime, gameStartDateTime);
        this.id = id;
        this.gameName = gameName;
        this.playground = playground;
        this.host = host;
        this.joinGameRequests = joinGameRequests;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sportsEvent = sportsEvent;
        this.homeScore = 0;
        this.awayScore = 0;
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
        this.isStarted = false;
        this.isFinished = false;
    }



    public CompetingTeam getCompetingTeamBySide(MatchTeamSide matchTeamSide) {
        if (matchTeamSide.equals(MatchTeamSide.HOME)) {
            return this.getHomeTeam();
        }
            return this.getAwayTeam();
    }

    private void validate(User host, Long runningTime, LocalDateTime gameStartDateTime) {
        /*if(host == null) {
            throw new UserNotExistException();
        }*/
        if(runningTime < 0 || runningTime > 120) {
            throw new IllegalArgumentException("경기 소요 시간이 올바르지 않습니다.");
        }
        if (LocalDateTime.now().isAfter(gameStartDateTime)) {
            throw new IllegalArgumentException("현재보다 전에 시작할 수 없습니다.");
        }
    }

    public boolean gameOnGoing(LocalDateTime currentTime) {
        LocalDateTime gameEndDateTime = gameStartDateTime.plusMinutes(runningTime);
        return currentTime.isAfter(gameStartDateTime) && currentTime.isBefore(gameEndDateTime);
    }

    public boolean isDayGame(LocalDateTime day) {
        return gameStartDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .equals(day.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public boolean isTimeRangeOverlapping(LocalDateTime time, Long gameRunningTime) {
        return gameStartDateTime.withSecond(0).isAfter(time.withSecond(0).plusMinutes(gameRunningTime)) &&
                gameStartDateTime.withSecond(0).plusMinutes(runningTime).isBefore(time.withSecond(0));
    }
}
