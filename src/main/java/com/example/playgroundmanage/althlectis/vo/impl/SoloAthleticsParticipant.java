package com.example.playgroundmanage.althlectis.vo.impl;

import com.example.playgroundmanage.althlectis.service.impl.RankAthleticsGenerator;
import com.example.playgroundmanage.althlectis.vo.*;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SoloAthleticsParticipant extends AthleticsParticipant {

    @Builder
    public SoloAthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, GameRecord gameRecord, boolean isAccepted, Athletics athletics) {
        super(id, gameTeamSide, user, gameRecord, isAccepted, athletics);

    }

    public static SoloAthleticsParticipant of(
            GameTeamSide gameTeamSide,
            User user,
            RankAthletics rankAthletics
    ) {
        return SoloAthleticsParticipant.builder()
                .user(user)
                .athletics(rankAthletics)
                .gameTeamSide(gameTeamSide)
                .isAccepted(true)
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
