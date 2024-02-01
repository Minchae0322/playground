package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.location.vo.Playground;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<GameRequest> gameRequests = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameParticipant> gameParticipants;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CompetingTeam> competingTeams = new ArrayList<>();

    @Enumerated
    private SportsEvent sportsEvent;

    private GameType gameType;

    private int homeScore;

    private int awayScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime gameStartDateTime;

    private Integer runningTime;


    @Builder
    public Game(String gameName, Playground playground, User host, SportsEvent sportsEvent, LocalDateTime gameStartDateTime, Integer runningTime, GameType gameType) {
        validate(runningTime, gameStartDateTime);
        this.gameName = (gameName == null || gameName.trim().isEmpty()) ? host.getNickname() + "的比赛" : gameName;
        this.playground = playground;
        this.host = host;
        this.sportsEvent = sportsEvent;
        this.homeScore = 0;
        this.awayScore = 0;
        this.gameType = gameType;
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
    }


    public GameDto toGameDto() {
        return GameDto.builder()
                .host(host)
                .playground(playground)
                .gameName(gameName)
                .gameId(id)
                .participantNum(gameParticipants.size())
                .startDateTime(MyDateTimeLocal.initMyDateTime(gameStartDateTime))
                .gameType(gameType)
                .runningTime(runningTime)
                .sportsEvent(sportsEvent)
                .build();
    }

    public Optional<CompetingTeam> getCompetingTeamBySide(GameTeamSide gameTeamSide) {
        return competingTeams.stream()
                .filter(competingTeam -> competingTeam.getGameTeamSide().equals(gameTeamSide))
                .findFirst();
    }



    private void validate(Integer runningTime, LocalDateTime gameStartDateTime) {
        if (runningTime < 0 || runningTime > 120) {
            throw new RuntimeException("경기 소요 시간이 올바르지 않습니다.");
        }
        if (LocalDateTime.now().isAfter(gameStartDateTime)) {
            throw new RuntimeException("현재보다 전에 시작할 수 없습니다.");
        }
    }

    public void addCompetingTeam(CompetingTeam competingTeam) {
        competingTeams.add(competingTeam);
    }


    public boolean isGameOngoing(LocalDateTime currentTime) {
        LocalDateTime gameEndDateTime = gameStartDateTime.plusMinutes(runningTime);
        return currentTime.isAfter(gameStartDateTime) && currentTime.isBefore(gameEndDateTime);
    }




}
