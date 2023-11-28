package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.MatchRegistration;
import com.example.playgroundmanage.dto.MatchTeamRegistration;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.repository.MatchParticipantRepository;
import com.example.playgroundmanage.repository.MatchParticipantTeamRepository;
import com.example.playgroundmanage.repository.MatchRepository;
import com.example.playgroundmanage.repository.MatchTeamRepository;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchTeamRepository matchTeamRepository;

    private final MatchParticipantTeamRepository matchParticipantTeamRepository;

    private final MatchRepository matchRepository;

    private final MatchParticipantRepository matchParticipantRepository;

    public List<MatchParticipantTeam> getTeamParticipatedMatch(Team team) {
        return matchParticipantTeamRepository.findAllByTeam(team);
    }

    public Match startMatch(MatchRegistration matchRegistration) {
        Match match = Match.builder()
                .host(matchRegistration.getHost())
                .matchStart(matchRegistration.getMatchStart())
                .sportsEvent(matchRegistration.getSportsEvent())
                .homeTeam(initMatchTeam())
                .awayTeam(initMatchTeam())
                .homeScore(0)
                .awayScore(0)
                .runningTime(matchRegistration.getRunningTime())
                .isStarted(false)
                .isFinished(false)
                .build();
        return matchRepository.save(match);
    }

    private MatchTeam initMatchTeam() {
        return MatchTeam.builder()
                .matchResult(MatchResult.NONE)
                .build();
    }


    public Match addHomeTeam(MatchTeamRegistration matchTeamRegistration) {
        Match match = matchRepository.findById(matchTeamRegistration.getMatchId())
                .orElseThrow(MatchNotExistException::new);
        participantTeamInMatch(match.getHomeTeam(), matchTeamRegistration.getTeam());
        participantUsersInMatch(match.getHomeTeam(), matchTeamRegistration.getUsers());
        return match;
    }

    public MatchParticipantTeam participantTeamInMatch(MatchTeam matchTeam, Team team) {
        return matchParticipantTeamRepository.save(MatchParticipantTeam.builder()
                .matchTeam(matchTeam)
                .team(team)
                .build());
    }

    public MatchParticipant participantUserInMatch(MatchTeam matchTeam, User user) {
       return matchParticipantRepository.save(MatchParticipant.builder()
                .matchTeam(matchTeam)
                .user(user)
                .build());
    }

    public List<MatchParticipant> participantUsersInMatch(MatchTeam matchTeam, List<User> users) {
        return users.stream()
                .map(u -> participantUserInMatch(matchTeam, u))
                .toList();
    }



}
