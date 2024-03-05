package com.example.playgroundmanage.team.finder;

import com.example.playgroundmanage.dto.TeamJoinRequestDto;
import com.example.playgroundmanage.team.dto.TeamDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeamFinder {

    List<TeamDto.TeamResponseDto> getTeams(TeamJoinRequestDto teamJoinRequestDto);

    String getType();
}
