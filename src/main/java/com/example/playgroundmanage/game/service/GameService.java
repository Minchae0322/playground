package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.MatchRegistration;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.game.match.GameRequestProcess;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final CompetingTeamRepository competingTeamRepository;

    private final TeamService teamService;

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;
    private final GameRequestProcess gameRequestProcess;

    private final SubTeamRepository subTeamRepository;

    private final MatchParticipantRepository matchParticipantRepository;



    @Transactional
    public Long createGame(MatchRegistration matchRegistration) {
        CompetingTeam homeTeam = initCompetingTeam(MatchTeamSide.HOME);
        CompetingTeam awayTeam = initCompetingTeam(MatchTeamSide.AWAY);

        Game game = Game.builder()
                .host(matchRegistration.getHost())
                .gameStartDateTime(matchRegistration.getMatchStart())
                .sportsEvent(matchRegistration.getSportsEvent())
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .runningTime(matchRegistration.getRunningTime())
                .build();

        generateSoloSubTeam(homeTeam);
        generateSoloSubTeam(awayTeam);
        return gameRepository.save(game).getId();
    }

    private CompetingTeam initCompetingTeam(MatchTeamSide matchTeamSide) {
        return competingTeamRepository.save(CompetingTeam.builder()
                .matchResult(MatchResult.NONE)
                .matchTeamSide(matchTeamSide)
                .build());
    }

    private void generateSoloSubTeam(CompetingTeam competingTeam) {
        if(competingTeam.isContainSoloTeam()) {
            throw new IllegalArgumentException("개인 팀이 이미 있습니다.");
        }
        subTeamRepository.save(SubTeam.builder()
                .isNoneTeam(true)
                .isAccept(true)
                .competingTeam(competingTeam)
                .build());
    }




    public Game getMatch(Long matchId) {
        return gameRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
