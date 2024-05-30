package com.example.playgroundmanage.althlectis.vo;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.login.dto.UserResponseDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class AthleticsParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GameTeamSide gameTeamSide;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Athletics athletics;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AthleticsSubTeam subTeam;
    @Enumerated
    private GameRecord gameRecord;

    private boolean isAccepted;

    @Builder
    public AthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, Athletics athletics, AthleticsSubTeam subTeam, GameRecord gameRecord, boolean isAccepted) {
        this.id = id;
        this.gameTeamSide = gameTeamSide;
        this.user = user;
        this.athletics = athletics;
        this.subTeam = subTeam;
        this.gameRecord = gameRecord;
        this.isAccepted = isAccepted;
    }
}
