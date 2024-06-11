package com.example.playgroundmanage.request.dto;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.request.vo.impl.athletics.FriendlyAthleticsJoinRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.GameTeamSide;

import java.time.LocalDateTime;

public record PendingRequestResponse (
        Long requestId,
        LocalDateTime requestTime,
        String userProfileImg,
        Long gameId,
        String gameName,
        Long teamId,
        String teamName,
        Long userId,
        String username,
        String requestType,
        String gameTeamSide
) {
    public static PendingRequestResponse of(FriendlyAthleticsJoinRequest athleticsRequest) {
        return new PendingRequestResponse(
                athleticsRequest.getId(),
                athleticsRequest.getModifiedAt(),
                athleticsRequest.getUser().getUserProfileImg().getFileUrl(),
                athleticsRequest.getAthletics().getId(),
                athleticsRequest.getAthletics().getGameName(),
                null,
                "",
                athleticsRequest.getUser().getId(),
                athleticsRequest.getUser().getNickname(),
                "friendlyGameJoin",
                athleticsRequest.getGameTeamSide().getValue()
        );
    }
}
