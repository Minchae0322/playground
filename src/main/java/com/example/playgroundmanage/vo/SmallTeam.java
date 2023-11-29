package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
public class SmallTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private MatchTeam matchTeam;

    @ManyToOne
    private Team team;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MatchParticipant> matchParticipants = new ArrayList<>();

    private boolean isNoneTeam;

    @Builder
    public SmallTeam(Long id, MatchTeam matchTeam, Team team, List<MatchParticipant> matchParticipants, boolean isNoneTeam) {
        this.id = id;
        this.matchTeam = matchTeam;
        this.team = team;
        this.matchParticipants = matchParticipants;
        this.isNoneTeam = isNoneTeam;
    }




    public boolean isContainTeam(Team team) {
        if(isNoneTeam) {
            return false;
        }
        return team.getId() == team.getId();
    }
}
