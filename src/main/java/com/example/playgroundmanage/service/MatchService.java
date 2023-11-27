package com.example.playgroundmanage.service;

import com.example.playgroundmanage.repository.MatchParticipantTeamRepository;
import com.example.playgroundmanage.vo.MatchParticipantTeam;
import com.example.playgroundmanage.vo.MatchTeam;
import com.example.playgroundmanage.vo.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchParticipantTeamRepository matchParticipantTeamRepository;

    public  List<MatchParticipantTeam> getTeamParticipatedMatch(Team team) {
        return matchParticipantTeamRepository.findAllByTeam(team);
    }

}
