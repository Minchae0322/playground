package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.MatchRegistration;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.UserJoinTeamParams;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.match.MatchRequestProcess;
import com.example.playgroundmanage.repository.*;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.vo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final MatchTeamRepository matchTeamRepository;

    private final TeamService teamService;

    private final MatchRepository matchRepository;

    private final MatchRequestProcess matchRequestProcess;

    private final SubTeamRepository subTeamRepository;

    private final MatchParticipantRepository matchParticipantRepository;



    public Game startMatch(MatchRegistration matchRegistration) {
        Game game = Game.builder()
                .host(matchRegistration.getHost())
                .matchStart(matchRegistration.getMatchStart())
                .sportsEvent(matchRegistration.getSportsEvent())
                .homeTeam(initCompetingTeam(MatchTeamSide.HOME))
                .awayTeam(initCompetingTeam(MatchTeamSide.AWAY))
                .runningTime(matchRegistration.getRunningTime())
                .build();
        return matchRepository.save(game);
    }

    private CompetingTeam initCompetingTeam(MatchTeamSide matchTeamSide) {
        return CompetingTeam.builder()
                .matchResult(MatchResult.NONE)
                .matchTeamSide(matchTeamSide)
                .build();
    }

    public Long generateSubTeam(SubTeamRegistrationParams subTeamRegistrationParams) {
        CompetingTeam competingTeam = matchTeamRepository.findById(subTeamRegistrationParams.getMatchTeamId()).orElseThrow();
        Team team = teamService.findByTeamId(subTeamRegistrationParams.getTeamId());

        if(matchRequestProcess.isTeamAlreadyInMatchTeamQueue(competingTeam, team)) {
            throw new IllegalArgumentException();
        }

        return subTeamRepository.save(SubTeam.builder()
                .isNoneTeam(false)
                .team(team)
                .competingTeam(competingTeam)
                .build()).getId();
    }

    @Transactional
    public void joinUserInSubTeam(UserJoinTeamParams userJoinTeamParams) {
        SubTeam subTeam = subTeamRepository.findById(userJoinTeamParams.getSubTeamId()).orElseThrow();
        matchRequestProcess.joinSubTeam(subTeam, userJoinTeamParams.getUser());
    }




    public Game getMatch(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
