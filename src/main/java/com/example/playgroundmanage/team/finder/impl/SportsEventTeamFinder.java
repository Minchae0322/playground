package com.example.playgroundmanage.team.finder.impl;

import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.dto.response.TeamResponseDto;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.team.finder.TeamFinder;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SportsEventTeamFinder implements TeamFinder {

    private final TeamRepository teamRepository;

    private final FileHandler fileHandler;
    @Override
    public List<TeamResponseDto> getTeams(TeamRequestDto teamRequestDto) {
        List<Team> teams = teamRepository.findAllBySportsEvent(SportsEvent.valueOf(teamRequestDto.getSportsEvent()));

        return teams.stream()
                .map(team -> TeamResponseDto.builder()
                        .teamId(team.getId())
                        .teamName(team.getTeamName())
                        .teamDescription(team.getDescription())
                        .sportsEvent(team.getSportsEvent().getValue())
                        .teamProfileImg(fileHandler.extractFile(team.getTeamPic()))
                        .build())
                .toList();
    }

    @Override
    public String getType() {
        return "sportsEvent";
    }
}
