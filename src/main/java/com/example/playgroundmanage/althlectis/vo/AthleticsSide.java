package com.example.playgroundmanage.althlectis.vo;


import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.game.vo.SubTeam;
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

    @Enumerated
    private GameTeamSide gameTeamSide;

    @OneToMany(mappedBy = "athleticsSide", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<AthleticsSubTeam> subTeams = new ArrayList<>();


    @Builder
    public AthleticsSide(Long id, Athletics athletics, GameTeamSide gameTeamSide) {
        this.id = id;
        this.athletics = athletics;
        this.gameTeamSide = gameTeamSide;
    }
}
