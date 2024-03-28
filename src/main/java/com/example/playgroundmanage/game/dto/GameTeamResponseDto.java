package com.example.playgroundmanage.game.dto;


import com.example.playgroundmanage.dto.SubTeamDto;
import com.example.playgroundmanage.login.dto.UserInfoDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GameTeamResponseDto {

    private List<SubTeamDto> subTeams;

    private SubTeamDto soloTeam;

    private List<UserInfoDto> participants;

    @Builder
    public GameTeamResponseDto(List<SubTeamDto> subTeams, SubTeamDto soloTeam, List<UserInfoDto> participants) {
        this.subTeams = subTeams;
        this.soloTeam = soloTeam;
        this.participants = participants;
    }
}
