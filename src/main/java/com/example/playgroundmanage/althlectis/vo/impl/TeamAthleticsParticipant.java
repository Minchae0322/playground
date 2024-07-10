package com.example.playgroundmanage.althlectis.vo.impl;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.team.vo.Team;
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
public class TeamAthleticsParticipant extends AthleticsParticipant {

    @ManyToOne(cascade = CascadeType.MERGE)
    private Team team;

    @Builder
    public TeamAthleticsParticipant(Long id, GameTeamSide gameTeamSide, User user, GameRecord gameRecord, boolean isAccepted, Athletics athletics, GameTeamSide gameTeamSide1, Team team) {
        super(id, gameTeamSide, user, gameRecord, isAccepted, athletics);
        this.team = team;
    }

    public static TeamAthleticsParticipant of(
            GameTeamSide gameTeamSide,
            User user,
            RankAthletics rankAthletics,
            Team team
    ) {
        return TeamAthleticsParticipant.builder()
                .user(user)
                .athletics(rankAthletics)
                .gameTeamSide(gameTeamSide)
                .isAccepted(true)
                .team(team)
                .build();
    }
}
