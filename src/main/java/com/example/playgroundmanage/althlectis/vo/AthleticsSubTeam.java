package com.example.playgroundmanage.althlectis.vo;

import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.team.vo.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class AthleticsSubTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private CompetingTeam competingTeam;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AthleticsSide athleticsSide;

    @ManyToOne
    private Team team;

    @OneToMany(mappedBy = "subTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AthleticsParticipant> gameParticipants;

    private boolean isSoloTeam;

    private boolean isAccept;

    @Builder
    public AthleticsSubTeam(Long id, CompetingTeam competingTeam, AthleticsSide athleticsSide, Team team, List<AthleticsParticipant> gameParticipants, boolean isSoloTeam, boolean isAccept) {
        this.id = id;
        this.competingTeam = competingTeam;
        this.athleticsSide = athleticsSide;
        this.team = team;
        this.gameParticipants = gameParticipants;
        this.isSoloTeam = isSoloTeam;
        this.isAccept = isAccept;
    }
}
