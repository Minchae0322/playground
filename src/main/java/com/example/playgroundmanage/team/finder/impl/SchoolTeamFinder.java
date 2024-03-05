package com.example.playgroundmanage.team.finder.impl;

import com.example.playgroundmanage.dto.TeamJoinRequestDto;
import com.example.playgroundmanage.team.dto.TeamDto;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.team.finder.TeamFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchoolTeamFinder implements TeamFinder {

    private final TeamRepository teamRepository;

    private final FileHandler fileHandler;

    @Override
    public List<TeamDto.TeamResponseDto> getTeams(TeamJoinRequestDto teamJoinRequestDto) {
        List<Team> teams = teamRepository.findAll();

        return teams.stream()
                .map(team -> TeamDto.TeamResponseDto.builder()
                        .teamId(team.getId())
                        .teamName(team.getTeamName())
                        .description(team.getDescription())
                        .sportsEvent(team.getSportsEvent().getValue_cn())
                        .teamProfileImg(fileHandler.extractFile(team.getTeamPic()))
                        .build())
                .toList();
    }

    @Override
    public String getType() {
        return "school";
    }

}
