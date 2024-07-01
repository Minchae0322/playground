package com.example.playgroundmanage.althlectis.vo;


import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.impl.SoloAthleticsParticipant;
import com.example.playgroundmanage.althlectis.vo.impl.TeamAthleticsParticipant;

import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class AthleticsSide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Athletics athletics;



    @Builder
    public AthleticsSide(Long id, Athletics athletics, GameTeamSide gameTeamSide, List<TeamAthleticsParticipant> teamAthleticsParticipants, List<SoloAthleticsParticipant> soloAthleticsParticipants) {
        this.id = id;
        this.athletics = athletics;
    }
}
