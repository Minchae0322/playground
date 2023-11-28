package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Entity
@Getter
@RequiredArgsConstructor
public class MatchParticipantTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Team team;

    @ManyToOne(cascade = CascadeType.MERGE)
    private MatchTeam matchTeam;

    @Builder
    public MatchParticipantTeam(Long id, Team team, MatchTeam matchTeam) {
        this.id = id;
        this.team = team;
        this.matchTeam = matchTeam;
    }
}
