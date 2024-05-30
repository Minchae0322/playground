package com.example.playgroundmanage.althlectis.dto.response;

import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.login.dto.UserResponseDto;

import java.util.List;

public record SubTeamResponse(
        Long teamId,
        String teamName,

        String teamDescription,

        Long subTeamId,

        String teamProfileImg,

        List<UserInTeamDetailsResponse>users
) {
    public static SubTeamResponse of(SubTeam subTeam) {
        return new SubTeamResponse(
                subTeam.getTeam().getId(),
                subTeam.getTeamName(),
                subTeam.getTeam().getDescription(),
                subTeam.getId(),
                subTeam.getTeam().getTeamProfileImg().getFileUrl(),
                subTeam.getGameParticipants().stream()
                        .map(UserInTeamDetailsResponse::of)
                        .toList()
        );
    }
}
