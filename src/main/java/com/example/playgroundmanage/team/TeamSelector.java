package com.example.playgroundmanage.team;

import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamSelector {

    public CompetingTeam getCompetingTeamBySide(Game game, GameTeamSide gameTeamSide) {
        return game.getCompetingTeamBySide(gameTeamSide).orElseThrow();
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
    public SubTeam getSoloSubTeam(Game game, GameTeamSide gameTeamSide) {
        CompetingTeam competingTeam = getCompetingTeamBySide(game, gameTeamSide);
        return getSoloSubTeam(competingTeam);
    }
}
