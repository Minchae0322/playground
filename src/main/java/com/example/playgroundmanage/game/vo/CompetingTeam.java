package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public SubTeam getSoloTeam() {
        return subTeams.stream()
                .filter(SubTeam::isSoloTeam)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public boolean isContainTeam(Team team) {
        return subTeams.stream()
                .filter(t -> t.isContainTeam(team))
                .toList().size() != 0;
    }

    @Transactional
    public List<SubTeam> getSubTeamsNotSoloTeam() {
        return this.getSubTeams().stream()
                .filter(ct -> !ct.isSoloTeam())
                .toList();
    }

    public boolean isContainSoloTeam() {
        return subTeams.stream()
                .filter(SubTeam::isSoloTeam)
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
