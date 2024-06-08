package com.example.playgroundmanage.request.vo;


import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.global.BaseEntity;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.RequestState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략 설정
@DiscriminatorColumn(name = "type") // 상속받는 클래스를 구분하는 컬럼
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AthleticsRequest extends BaseEntity implements Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User host;

    @Enumerated(EnumType.STRING)
    private RequestState requestState;

    @Enumerated(EnumType.STRING)
    private GameTeamSide gameTeamSide;
    @ManyToOne
    private Athletics athletics;

    public AthleticsRequest(Long id, User user, User host, RequestState requestState, GameTeamSide gameTeamSide, Athletics athletics) {
        this.id = id;
        this.user = user;
        this.host = host;
        this.requestState = requestState;
        this.gameTeamSide = gameTeamSide;
        this.athletics = athletics;
    }

    public void setRequestState(final RequestState requestState) {
        this.requestState = requestState;
    }
}
