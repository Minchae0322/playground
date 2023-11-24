package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.type.MatchResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class MatchTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Match match;

    @OneToMany(mappedBy = "matchTeam")
    private List<MatchParticipantTeam> matchParticipantTeams;

    @OneToMany(mappedBy = "matchTeam")
    private List<MatchParticipant> matchParticipants;

    private MatchResult matchResult;

}
