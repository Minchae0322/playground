package com.example.playgroundmanage.althlectis.dto.response;

import java.util.List;

public record SubTeamResponse(
        Long teamId,
        String teamName,

        String teamDescription,

        Long subTeamId,

        String teamProfileImg,

        List<UserInTeamDetailsResponse>users
) {
  /*  public static SubTeamResponse of(AthleticsSubTeam subTeam) {
        return new SubTeamResponse(
                subTeam.getTeam().getId(),
                subTeam.getTeam().getTeamName(),
                subTeam.getTeam().getDescription(),
                subTeam.getId(),
                subTeam.getTeam().getTeamProfileImg().getFileUrl(),
                subTeam.getGameParticipants().stream()
                        .map(UserInTeamDetailsResponse::of)
                        .toList()
        );
    }*/
}
