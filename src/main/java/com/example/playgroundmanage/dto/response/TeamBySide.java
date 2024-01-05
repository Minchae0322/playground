package com.example.playgroundmanage.dto.response;


import com.example.playgroundmanage.dto.SubTeamDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class TeamBySide {

    private List<SubTeamDto> subTeams;

    private SubTeamDto soloTeam;

    private String matchTeamSide;

    @Builder
    public TeamBySide(List<SubTeamDto> subTeams, SubTeamDto soloTeam, String matchTeamSide) {
        this.subTeams = subTeams;
        this.soloTeam = soloTeam;
        this.matchTeamSide = matchTeamSide;
    }
}
