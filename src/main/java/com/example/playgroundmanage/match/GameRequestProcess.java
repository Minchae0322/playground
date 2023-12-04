package com.example.playgroundmanage.match;

import com.example.playgroundmanage.repository.MatchParticipantRepository;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GameRequestProcess {

    private final MatchParticipantRepository matchParticipantRepository;

    public boolean isTeamAlreadyInMatchTeamQueue(CompetingTeam competingTeam, Team team) {
        return competingTeam.isContainTeam(team);
    }

    public SubTeam getSoloTeam(Game game, MatchTeamSide matchTeamSide) {
        CompetingTeam competingTeam = game.getMatchTeamBySide(matchTeamSide);
        return competingTeam.getSubTeams().stream()
                .filter(SubTeam::isSoloTeam)
                .findFirst().orElseThrow();
    }

    public SubTeam getTeam(Game game, Team team, MatchTeamSide matchTeamSide) {
        CompetingTeam competingTeam = game.getMatchTeamBySide(matchTeamSide);
        return competingTeam.findSubTeam(team).orElse(SubTeam.builder()
                .isNoneTeam(false)
                .isAccept(true)
                .competingTeam(competingTeam)
                .build());
    }

    public boolean isUserParticipatingInGame(Game game, User user) {
        List<MatchParticipant> matchParticipantList = matchParticipantRepository.findAllByGame(game);
        return matchParticipantList.stream()
                .filter(p -> p.getUser().equals(user))
                .toList().size() != 0;
    }



}
