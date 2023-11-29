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



    public Match startMatch(MatchRegistration matchRegistration) {
        Match match = Match.builder()
                .host(matchRegistration.getHost())
                .matchStart(matchRegistration.getMatchStart())
                .sportsEvent(matchRegistration.getSportsEvent())
                .homeTeam(initMatchTeam(MatchTeamSide.HOME))
                .awayTeam(initMatchTeam(MatchTeamSide.AWAY))
                .runningTime(matchRegistration.getRunningTime())
                .build();
        return matchRepository.save(match);
    }

    private MatchTeam initMatchTeam(MatchTeamSide matchTeamSide) {
        return MatchTeam.builder()
                .matchResult(MatchResult.NONE)
                .matchTeamSide(matchTeamSide)
                .build();
    }

    public boolean isTeamAlreadyInMatchTeamQueue(MatchTeam matchTeam, Team team) {
        return matchTeam.isContainTeam(team);
    }

    public Long generateSmallTeam(SmallTeamRegistration smallTeamRegistration) {
        MatchTeam matchTeam = matchTeamRepository.findById(smallTeamRegistration.getMatchTeamId()).orElseThrow();
        Team team = teamService.findByTeamId(smallTeamRegistration.getTeamId());

        if(isTeamAlreadyInMatchTeamQueue(matchTeam, team)) {
            throw new IllegalArgumentException();
        }

        return smallTeamRepository.save(SmallTeam.builder()
                .isNoneTeam(false)
                .team(team)
                .matchTeam(matchTeam)
                .build()).getId();
    }

    public void addSoloSmallTeam(Long matchTeamId) {
        MatchTeam matchTeam = matchTeamRepository.findById(matchTeamId).orElseThrow();
        smallTeamRepository.save(SmallTeam.builder()
                .isNoneTeam(true)
                .matchTeam(matchTeam)
                .build());
    }


    public Long userJoinSmallTeam(Long smallTeamId, User user) {
        SmallTeam smallTeam = smallTeamRepository.findById(smallTeamId).orElseThrow();
        return matchParticipantRepository.save(MatchParticipant.builder()
                .user(user)
                .smallTeam(smallTeam)
                .build()).getId();
    }

    @Transactional
    public void joinMatchTeamAsSolo(Long matchTeamId, User user) {
        MatchTeam matchTeam = matchTeamRepository.findById(matchTeamId).orElseThrow();
        SmallTeam smallTeam = matchTeam.getSmallTeams().stream()
                .filter(SmallTeam::isSoloTeam)
                .findFirst().orElseThrow();
        userJoinSmallTeam(smallTeam.getId(), user);
    }




    public Match getMatch(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
