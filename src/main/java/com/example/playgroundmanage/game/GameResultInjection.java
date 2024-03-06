package com.example.playgroundmanage.game;


import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.GameResult;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameResultInjection {

    private final GameParticipantRepository gameParticipantRepository;

    @Transactional
    public void injectResult(Game game, GameResult gameResult) {
        updateGameParticipantsRecord(findGameParticipantsByTeamSide(game, GameTeamSide.HOME), gameResult.getHomeScore(), gameResult.getAwayScore());
        updateGameParticipantsRecord(findGameParticipantsByTeamSide(game, GameTeamSide.AWAY), gameResult.getAwayScore(), gameResult.getHomeScore());
    }

    private void updateGameParticipantsRecord(List<GameParticipant> teamParticipants, int teamScore, int opponentScore) {
        GameRecord record = (teamScore > opponentScore) ? GameRecord.WIN : GameRecord.LOSE;

        for (GameParticipant participant : teamParticipants) {
            participant.updateGameRecord(record);
        }

        gameParticipantRepository.saveAll(teamParticipants);
    }

    private List<GameParticipant> findGameParticipantsByTeamSide(Game game, GameTeamSide gameTeamSide) {
        CompetingTeam teamSideCompetingTeam = game.getCompetingTeams().stream()
                .filter(competingTeam -> competingTeam.getGameTeamSide().equals(gameTeamSide))
                .findFirst()
                .orElseThrow();

        return teamSideCompetingTeam.getSubTeams().stream()
                .flatMap(subTeam -> subTeam.getGameParticipants().stream())
                .collect(Collectors.toList());
    }

}
