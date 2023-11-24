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

    @OneToMany(mappedBy = "match_team")
    private List<MatchParticipantTeam> matchParticipantTeams;

    @OneToMany(mappedBy = "match_team")
    private List<MatchParticipant> matchParticipants;

    private MatchResult matchResult;

}
