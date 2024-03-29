package com.example.playgroundmanage.game.dto;


import com.example.playgroundmanage.dto.SubTeamDto;
import com.example.playgroundmanage.login.dto.UserResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GameTeamResponseDto {

    private List<SubTeamDto> subTeams;

    private SubTeamDto soloTeam;

    private List<UserResponseDto> participants;

    @Builder
    public GameTeamResponseDto(List<SubTeamDto> subTeams, SubTeamDto soloTeam, List<UserResponseDto> participants) {
        this.subTeams = subTeams;
        this.soloTeam = soloTeam;
        this.participants = participants;
    }
}
