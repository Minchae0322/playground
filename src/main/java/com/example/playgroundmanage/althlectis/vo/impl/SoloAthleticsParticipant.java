package com.example.playgroundmanage.althlectis.vo.impl;

import com.example.playgroundmanage.althlectis.vo.*;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SoloAthleticsParticipant extends AthleticsParticipant {

    @ManyToOne(cascade = CascadeType.MERGE)
    private AthleticsSide athleticsSide;

    @Builder
    public SoloAthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, GameRecord gameRecord, boolean isAccepted, Athletics athletics, AthleticsSide athleticsSide) {
        super(id, gameTeamSide, user, gameRecord, isAccepted, athletics);
        this.athleticsSide = athleticsSide;
    }

    public static SoloAthleticsParticipant of(
            GameTeamSide gameTeamSide,
            User user,
            AthleticsSide athleticsSide
    ) {
        return SoloAthleticsParticipant.builder()
                .user(user)
                .gameTeamSide(gameTeamSide)
                .isAccepted(true)
                .athleticsSide(athleticsSide)
                .build();
    }

    public static SoloAthleticsParticipant of(
            User user,
            FriendlyAthletics friendlyAthletics
    ) {
        return SoloAthleticsParticipant.builder()
                .user(user)
                .gameTeamSide(GameTeamSide.NONE)
                .athletics(friendlyAthletics)
                .isAccepted(true)
                .build();
    }

}
