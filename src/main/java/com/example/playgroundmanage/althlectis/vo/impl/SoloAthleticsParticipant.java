package com.example.playgroundmanage.althlectis.vo.impl;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SoloAthleticsParticipant extends AthleticsParticipant {

    @Builder
    public SoloAthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, Athletics athletics, GameRecord gameRecord, boolean isAccepted) {
        super(id, gameTeamSide, user, athletics, gameRecord, isAccepted);
    }
}
