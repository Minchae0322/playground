package com.example.playgroundmanage.althlectis.vo.impl;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.althlectis.vo.AthleticsSubTeam;
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
public class TeamAthleticsParticipant extends AthleticsParticipant {
    @ManyToOne(cascade = CascadeType.MERGE)
    private AthleticsSubTeam subTeam;

    @Builder
    public TeamAthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, Athletics athletics, GameRecord gameRecord, boolean isAccepted, AthleticsSubTeam subTeam) {
        super(id, gameTeamSide, user, athletics, gameRecord, isAccepted);
        this.subTeam = subTeam;
    }
}
