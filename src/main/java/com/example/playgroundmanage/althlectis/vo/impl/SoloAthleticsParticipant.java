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

    @ManyToOne(cascade = CascadeType.MERGE)
    private FriendlyAthletics friendlyAthletics;

    @Builder
    public SoloAthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, GameRecord gameRecord, boolean isAccepted, AthleticsSide athleticsSide, FriendlyAthletics friendlyAthletics) {
        super(id, gameTeamSide, user, gameRecord, isAccepted);
        this.athleticsSide = athleticsSide;
        this.friendlyAthletics = friendlyAthletics;
    }
}
