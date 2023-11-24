package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Entity
@Getter
@RequiredArgsConstructor
public class MatchParticipantTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Team team;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private MatchTeam matchTeam;


}
