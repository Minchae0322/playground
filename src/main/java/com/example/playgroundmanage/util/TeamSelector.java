package com.example.playgroundmanage.util;

import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamSelector {

    public CompetingTeam getCompetingTeamBySide(Game game, MatchTeamSide matchTeamSide) {
        if (matchTeamSide == MatchTeamSide.HOME) {
            return game.getHomeTeam();
        }
        return game.getAwayTeam();
    }

    @Transactional
    public List<SubTeam> getSubTeamsNotSoloTeam(CompetingTeam competingTeam) {
        return competingTeam.getSubTeams().stream()
                .filter(ct -> !ct.isSoloTeam())
                .toList();
    }

    public SubTeam getSoloSubTeam(CompetingTeam competingTeam) {
        return competingTeam.getSubTeams().stream()
                .filter(SubTeam::isSoloTeam)
                .findFirst().orElseThrow();
    }
    public SubTeam getSoloSubTeam(Game game, MatchTeamSide matchTeamSide) {
        CompetingTeam competingTeam = getCompetingTeamBySide(game, matchTeamSide);
        return getSoloSubTeam(competingTeam);
    }
}
