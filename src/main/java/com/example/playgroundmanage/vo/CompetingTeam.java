package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@RequiredArgsConstructor
public class CompetingTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "homeTeam", cascade = CascadeType.MERGE)
    private Game game;

    @Enumerated
    private MatchTeamSide matchTeamSide;

    @OneToMany(mappedBy = "competingTeam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<SubTeam> subTeams = new ArrayList<>();

    @Enumerated
    private MatchResult matchResult;

    @Builder
    public CompetingTeam(Long id, Game game, MatchTeamSide matchTeamSide, MatchResult matchResult) {
        this.id = id;
        this.game = game;
        this.matchTeamSide = matchTeamSide;
        this.matchResult = matchResult;
    }

    public boolean isContainTeam(Team team) {
        return subTeams.stream()
                .filter(t -> t.isContainTeam(team))
                .toList().size() != 0;
    }

    public boolean isContainSoloTeam() {
        return subTeams.stream()
                .filter(SubTeam::isNoneTeam)
                .toList().size() != 0;
    }

    public Optional<SubTeam> findSubTeam(Team team) {
        for(SubTeam subTeam : subTeams) {
            if(subTeam.isContainTeam(team)) {
                return Optional.of(subTeam);
            }
        }
        return Optional.empty();
    }
}
