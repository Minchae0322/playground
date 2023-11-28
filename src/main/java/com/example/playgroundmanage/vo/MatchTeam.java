package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.type.MatchResult;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@RequiredArgsConstructor
public class MatchTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "homeTeam", cascade = CascadeType.MERGE)
    private Match match;

    @OneToMany(mappedBy = "matchTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchParticipantTeam> matchParticipantTeams;


    @OneToMany(mappedBy = "matchTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchParticipant> matchParticipants;

    private MatchResult matchResult;

    @Builder
    public MatchTeam(Long id, Match match, List<MatchParticipantTeam> matchParticipantTeams, List<MatchParticipant> matchParticipants, MatchResult matchResult) {
        this.id = id;
        this.match = match;
        this.matchParticipantTeams = matchParticipantTeams;
        this.matchParticipants = matchParticipants;
        this.matchResult = matchResult;
    }
}
