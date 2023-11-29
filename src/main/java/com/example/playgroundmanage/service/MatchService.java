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

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchTeamRepository matchTeamRepository;

    private final MatchParticipantTeamRepository matchParticipantTeamRepository;

    private final MatchRepository matchRepository;


    private final SmallTeamRepository smallTeamRepository;

    private final MatchParticipantRepository matchParticipantRepository;

    public List<MatchParticipantTeam> getTeamParticipatedMatch(Team team) {
        return matchParticipantTeamRepository.findAllByTeam(team);
    }

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

    @Transactional
    public Long generateSmallTeam(SmallTeamRegistration smallTeamRegistration) {
        Match match = getMatch(smallTeamRegistration.getMatchId());
        MatchTeam matchTeam = match.getMatchTeamBySide(smallTeamRegistration.getMatchTeamSide());

        if(isTeamAlreadyInMatchTeamQueue(matchTeam, smallTeamRegistration.getTeam())) {
            throw new IllegalArgumentException();
        }

        return smallTeamRepository.save(SmallTeam.builder()
                .isNoneTeam(true)
                .matchTeam(matchTeam)
                .build()).getId();
    }


    public Long teamJoinSmallTeam(Long smallTeamId, Team team) {
        SmallTeam smallTeam = smallTeamRepository.findById(smallTeamId).orElseThrow();
        return matchParticipantTeamRepository.save(MatchParticipantTeam.builder()
                .team(team)
                .smallTeam(smallTeam)
                .build()).getId();
    }


    public Long userJoinSmallTeam(Long smallTeamId, User user) {
        SmallTeam smallTeam = smallTeamRepository.findById(smallTeamId).orElseThrow();
        return matchParticipantRepository.save(MatchParticipant.builder()
                .user(user)
                .smallTeam(smallTeam)
                .build()).getId();
    }



    public Match getMatch(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
