package com.example.playgroundmanage.althlectis.dto.response;

import com.example.playgroundmanage.althlectis.vo.AthleticsSide;
import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.type.GameTeamSide;

import java.util.List;

public record AthleticsDetailsResponse(
        List<SubTeamResponse> subTeams,
        List<UserInTeamDetailsResponse> participants

) {
  /*  public static AthleticsDetailsResponse of(AthleticsSide athleticsSide) {
        return new AthleticsDetailsResponse(
                athleticsSide.getSubTeams().stream()
                        .map(SubTeamResponse::of)
                        .toList(),
                athleticsSide.getAthletics().getAthleticsParticipants().stream()
                        .filter(athleticsParticipant -> athleticsParticipant.getGameTeamSide().equals(athleticsSide.getGameTeamSide()))
                        .map(UserInTeamDetailsResponse::of)
                        .toList()
        );
    }*/

    public static AthleticsDetailsResponse of(FriendlyAthletics friendlyAthletics) {
        return new AthleticsDetailsResponse(
                null,
                friendlyAthletics.getAthleticsParticipants().stream()
                        .filter(athleticsParticipant -> athleticsParticipant.getGameTeamSide().equals(GameTeamSide.NONE))
                        .map(UserInTeamDetailsResponse::of)
                        .toList()
        );
    }


}
