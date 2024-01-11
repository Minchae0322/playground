package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략 설정
@DiscriminatorColumn(name = "type") // 상속받는 클래스를 구분하는 컬럼
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GameRequest extends Request{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Game game;

    @ManyToOne
    private User user;

    @ManyToOne
    private User host;


    protected MatchTeamSide matchTeamSide;

    protected LocalDateTime expiredTime;


    protected LocalDateTime requestTime;


    public GameRequest(Long id, Game game, User user, User host, MatchTeamSide matchTeamSide, LocalDateTime expiredTime, LocalDateTime requestTime) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.host = host;
        this.matchTeamSide = matchTeamSide;
        this.expiredTime = expiredTime;
        this.requestTime = requestTime;
    }

    public abstract RequestInfoDto toGameRequestInfoDto();

    /*public GameRequestDto toGameRequestDto() {
        GameRequestDto gameRequestDto = GameRequestDto.builder()
                .matchTeamSide(matchTeamSide)
                .requestTime(requestTime)
                .requestId(id)
                .user(user)
                .game(game)
                .build();

        if (this instanceof SoloGameJoinRequest) {
            gameRequestDto.setRequestType("soloGameJoin");
        }

        if (this instanceof TeamGameJoinRequest) {
            TeamGameJoinRequest teamRequest = (TeamGameJoinRequest) this;
            gameRequestDto.setRequestType("teamGameJoin");
            gameRequestDto.setSubTeam(teamRequest.getSubTeam());
        }

        if (this instanceof TeamGameRegistrationRequest) {
            TeamGameRegistrationRequest  teamGameRegistrationRequest = (TeamGameRegistrationRequest) this;
            gameRequestDto.setRequestType("teamGameRegistration");
            gameRequestDto.setTeam(teamGameRegistrationRequest.getTeam());
        }

        return gameRequestDto;
    }*/
}
