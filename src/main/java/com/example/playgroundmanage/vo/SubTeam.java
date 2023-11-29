package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
public class SubTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private CompetingTeam competingTeam;

    @ManyToOne
    private Team team;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MatchParticipant> matchParticipants = new ArrayList<>();

    private boolean isNoneTeam;

    @Builder
    public SubTeam(Long id, CompetingTeam competingTeam, Team team, List<MatchParticipant> matchParticipants, boolean isNoneTeam) {
        this.id = id;
        this.competingTeam = competingTeam;
        this.team = team;
        this.matchParticipants = matchParticipants;
        this.isNoneTeam = isNoneTeam;
    }


    public boolean isSoloTeam() {
        return this.isNoneTeam;
    }




    public boolean isContainTeam(Team team) {
        if(isNoneTeam) {
            return false;
        }
        return team.getId() == team.getId();
    }
}
