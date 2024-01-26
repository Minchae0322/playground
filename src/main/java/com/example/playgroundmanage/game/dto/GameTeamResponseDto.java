package com.example.playgroundmanage.game.dto;


import com.example.playgroundmanage.dto.SubTeamDto;
import com.example.playgroundmanage.game.vo.SubTeam;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GameTeamResponseDto {

    private List<SubTeamDto> subTeams;

    private SubTeamDto soloTeam;

    @Builder
    public GameTeamResponseDto(List<SubTeamDto> subTeams, SubTeamDto soloTeam) {
        this.subTeams = subTeams;
        this.soloTeam = soloTeam;
    }
}