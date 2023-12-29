package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.location.vo.Playground;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.playgroundmanage.util.DateFormat.dateFormatWith0Second;
import static com.example.playgroundmanage.util.DateFormat.dateFormatYYYYMMDD;

@Entity
@Setter
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

    private boolean isFriendly;

    private int homeScore;

    private int awayScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime gameStartDateTime;

    private Integer runningTime;



    @Builder
    public Game(Long id, String gameName, Playground playground, User host, SportsEvent sportsEvent, LocalDateTime gameStartDateTime, Integer runningTime, boolean isFriendly) {
        validate(host, runningTime, gameStartDateTime);
        this.id = id;
        this.gameName = gameName;
        this.playground = playground;
        this.host = host;
        this.homeTeam = initCompetingTeam(MatchTeamSide.HOME);
        this.awayTeam = initCompetingTeam(MatchTeamSide.AWAY);
        this.sportsEvent = sportsEvent;
        this.homeScore = 0;
        this.awayScore = 0;
        this.isFriendly = isFriendly;
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
    }

    public GameDto toGameDto() {
        return GameDto.builder()
                .host(host)
                .gameName(gameName)
                .gameId(id)
                .startDateTime(MyDateTimeLocal.initMyDateTime(gameStartDateTime))
                .isFriendly(isFriendly)
                .runningTime(runningTime)
                .sportsEvent(sportsEvent)
                .build();
    }

    public CompetingTeam getCompetingTeamBySide(MatchTeamSide matchTeamSide) {
        if (matchTeamSide.equals(MatchTeamSide.HOME)) {
            return this.getHomeTeam();
        }
            return this.getAwayTeam();
    }

    private CompetingTeam initCompetingTeam(MatchTeamSide matchTeamSide) {
        return CompetingTeam.builder()
                .matchResult(MatchResult.NONE)
                .matchTeamSide(matchTeamSide)
                .build();
    }

    private void validate(User host, Integer runningTime, LocalDateTime gameStartDateTime) {
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

    public LocalDateTime getGameEndDateTime() {
        return this.gameStartDateTime.plusMinutes(runningTime);
    }

    public boolean isGameOngoing(LocalDateTime currentTime) {
        LocalDateTime gameEndDateTime = gameStartDateTime.plusMinutes(runningTime);
        return currentTime.isAfter(gameStartDateTime) && currentTime.isBefore(gameEndDateTime);
    }

    public boolean isGameDay(LocalDateTime dateTime) {
        String gameStartFormat = dateFormatYYYYMMDD(gameStartDateTime);
        String dateTimeFormat = dateFormatYYYYMMDD(dateTime);
        return gameStartFormat.equals(dateTimeFormat);
    }

    public boolean isTimeRangeOverlapping(LocalDateTime time, Integer gameRunningTime) {
        LocalDateTime startTimeNormalized = dateFormatWith0Second(time);
        LocalDateTime endTimeNormalized = dateFormatWith0Second(time).plusMinutes(gameRunningTime);

        LocalDateTime gameStartTimeNormalized = dateFormatWith0Second(gameStartDateTime);
        LocalDateTime gameEndTimeNormalized = dateFormatWith0Second(gameStartDateTime).plusMinutes(runningTime);

        return !(gameEndTimeNormalized.isBefore(startTimeNormalized) || gameStartTimeNormalized.isAfter(endTimeNormalized));
    }
}
