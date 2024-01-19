package com.example.playgroundmanage.util.impl;

import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.dto.response.TeamResponseDto;
import com.example.playgroundmanage.game.repository.TeamRepository;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import com.example.playgroundmanage.util.TeamFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchoolTeamFinder implements TeamFinder {

    private final TeamRepository teamRepository;

    private final FileHandler fileHandler;

    @Override
    public List<TeamResponseDto> getTeams(TeamRequestDto teamRequestDto) {
        List<Team> teams = teamRepository.findAll();

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
        return "school";
    }

}
