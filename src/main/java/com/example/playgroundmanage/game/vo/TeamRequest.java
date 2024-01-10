package com.example.playgroundmanage.game.vo;


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
public abstract class TeamRequest extends Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Team team;

    @ManyToOne
    private User leader;

    private String introduction;


    protected LocalDateTime requestTime;


    public TeamRequest(Long id, User user, Team team, User leader, String introduction, LocalDateTime requestTime) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.leader = leader;
        this.introduction = introduction;
        this.requestTime = requestTime;
    }
}
