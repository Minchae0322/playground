package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Enumerated
    private MatchTeamSide matchTeamSide;

    @OneToMany(mappedBy = "matchTeam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<SmallTeam> smallTeams = new ArrayList<>();

    @Enumerated
    private MatchResult matchResult;

    @Builder
    public MatchTeam(Long id, Match match, MatchTeamSide matchTeamSide, MatchResult matchResult) {
        this.id = id;
        this.match = match;
        this.matchTeamSide = matchTeamSide;
        this.matchResult = matchResult;
    }

    public boolean isContainTeam(Team team) {
        return smallTeams.stream()
                .filter(t -> t.isContainTeam(team))
                .toList().size() != 0;
    }
}
