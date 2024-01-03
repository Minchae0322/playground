package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.game.vo.impl.SoloGameJoinRequest;
import com.example.playgroundmanage.game.vo.impl.TeamGameJoinRequest;
import com.example.playgroundmanage.game.vo.impl.TeamGameRegistrationRequest;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략 설정
@DiscriminatorColumn(name = "type") // 상속받는 클래스를 구분하는 컬럼
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GameRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Game game;

    @ManyToOne
    private User user;


    protected MatchTeamSide matchTeamSide;

    protected LocalDateTime expiredTime;


    protected LocalDateTime requestTime;


    public GameRequest(Long id, Game game, User user, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.matchTeamSide = matchTeamSide;
        this.expiredTime = expiredTime;
        this.requestTime = requestTime;
    }

    public GameRequestDto toGameRequestDto() {
        GameRequestDto gameRequestDto = GameRequestDto.builder()
                .matchTeamSide(matchTeamSide)
                .requestTime(requestTime)
                .user(user)
                .build();

        if (this instanceof SoloGameJoinRequest) {
            gameRequestDto.setRequestType("soloGameJoin");
        }

        if (this instanceof TeamGameJoinRequest) {
            TeamGameJoinRequest teamRequest = (TeamGameJoinRequest) this;
            gameRequestDto.setRequestType("teamGameJoin");
            gameRequestDto.setSubTeamId(teamRequest.getSubTeam().getId());
        }

        if (this instanceof TeamGameRegistrationRequest) {
            TeamGameRegistrationRequest  teamGameRegistrationRequest = (TeamGameRegistrationRequest) this;
            gameRequestDto.setRequestType("teamGameRegistration");
            gameRequestDto.setTeamId(teamGameRegistrationRequest.getTeam().getId());
        }

        return gameRequestDto;
    }

    public GameRequest update() {
        this.requestTime = LocalDateTime.now();
        return this;
    }
}
