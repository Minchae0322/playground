package com.example.playgroundmanage.althlectis.vo;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.login.dto.UserResponseDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략 설정
@DiscriminatorColumn(name = "type") // 상속받는 클래스를 구분하는 컬럼
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AthleticsParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GameTeamSide gameTeamSide;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @Enumerated
    private GameRecord gameRecord;

    private boolean isAccepted;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Athletics athletics;


    public AthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, GameRecord gameRecord, boolean isAccepted, Athletics athletics) {
        this.id = id;
        this.gameTeamSide = gameTeamSide;
        this.user = user;
        this.gameRecord = gameRecord;
        this.isAccepted = isAccepted;
        this.athletics = athletics;
    }

    public void updateAthleticsRecord(GameRecord gameRecord) {
        this.gameRecord = gameRecord;
    }
}
