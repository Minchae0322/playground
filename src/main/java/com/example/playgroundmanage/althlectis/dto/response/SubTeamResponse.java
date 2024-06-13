package com.example.playgroundmanage.althlectis.dto.response;

import com.example.playgroundmanage.althlectis.vo.impl.TeamAthleticsParticipant;
import com.example.playgroundmanage.team.vo.Team;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record SubTeamResponse(
        Long teamId,
        String teamName,

        String teamDescription,


        String teamProfileImg,

        List<UserInTeamDetailsResponse> users
) {
    public static SubTeamResponse of(Map.Entry<Team, List<TeamAthleticsParticipant>> map) {
        return new SubTeamResponse(
                map.getKey().getId(),
                map.getKey().getTeamName(),
                map.getKey().getDescription(),
                map.getKey().getTeamProfileImg().getFileUrl(),
                map.getValue().stream()
                        .map(UserInTeamDetailsResponse::of)
                        .collect(Collectors.toList())
        );
    }

}
