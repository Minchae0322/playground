package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.MatchRegistration;
import com.example.playgroundmanage.dto.SmallTeamRegistration;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.repository.*;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.vo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchTeamRepository matchTeamRepository;

    private final TeamService teamService;

    private final MatchRepository matchRepository;


    private final SmallTeamRepository smallTeamRepository;

    private final MatchParticipantRepository matchParticipantRepository;



    public Game startMatch(MatchRegistration matchRegistration) {
        Game game = Game.builder()
                .host(matchRegistration.getHost())
                .matchStart(matchRegistration.getMatchStart())
                .sportsEvent(matchRegistration.getSportsEvent())
                .homeTeam(initMatchTeam(MatchTeamSide.HOME))
                .awayTeam(initMatchTeam(MatchTeamSide.AWAY))
                .runningTime(matchRegistration.getRunningTime())
                .build();
        return matchRepository.save(game);
    }

    private CompetingTeam initMatchTeam(MatchTeamSide matchTeamSide) {
        return CompetingTeam.builder()
                .matchResult(MatchResult.NONE)
                .matchTeamSide(matchTeamSide)
                .build();
    }

    public boolean isTeamAlreadyInMatchTeamQueue(CompetingTeam competingTeam, Team team) {
        return competingTeam.isContainTeam(team);
    }

    public Long generateSmallTeam(SmallTeamRegistration smallTeamRegistration) {
        CompetingTeam competingTeam = matchTeamRepository.findById(smallTeamRegistration.getMatchTeamId()).orElseThrow();
        Team team = teamService.findByTeamId(smallTeamRegistration.getTeamId());

        if(isTeamAlreadyInMatchTeamQueue(competingTeam, team)) {
            throw new IllegalArgumentException();
        }

        return smallTeamRepository.save(SubTeam.builder()
                .isNoneTeam(false)
                .team(team)
                .competingTeam(competingTeam)
                .build()).getId();
    }

    public void addSoloSmallTeam(Long matchTeamId) {
        CompetingTeam competingTeam = matchTeamRepository.findById(matchTeamId).orElseThrow();
        smallTeamRepository.save(SubTeam.builder()
                .isNoneTeam(true)
                .competingTeam(competingTeam)
                .build());
    }


    public Long userJoinSmallTeam(Long smallTeamId, User user) {
        SubTeam subTeam = smallTeamRepository.findById(smallTeamId).orElseThrow();
        return matchParticipantRepository.save(MatchParticipant.builder()
                .user(user)
                .subTeam(subTeam)
                .build()).getId();
    }

    @Transactional
    public void joinMatchTeamAsSolo(Long matchTeamId, User user) {
        CompetingTeam competingTeam = matchTeamRepository.findById(matchTeamId).orElseThrow();
        SubTeam subTeam = competingTeam.getSubTeams().stream()
                .filter(SubTeam::isSoloTeam)
                .findFirst().orElseThrow();
        userJoinSmallTeam(subTeam.getId(), user);
    }




    public Game getMatch(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
