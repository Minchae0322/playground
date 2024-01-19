package com.example.playgroundmanage.team.finder;

import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.dto.response.TeamResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeamFinder {

    List<TeamResponseDto> getTeams(TeamRequestDto teamRequestDto);

    String getType();
}
