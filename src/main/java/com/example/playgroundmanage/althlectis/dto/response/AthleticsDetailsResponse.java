package com.example.playgroundmanage.althlectis.dto.response;

import com.example.playgroundmanage.althlectis.vo.AthleticsSide;
import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.althlectis.vo.impl.RankAthletics;
import com.example.playgroundmanage.althlectis.vo.impl.SoloAthleticsParticipant;
import com.example.playgroundmanage.althlectis.vo.impl.TeamAthleticsParticipant;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.GameTeamSide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AthleticsDetailsResponse(
        List<SubTeamResponse> subTeams,
        List<UserInTeamDetailsResponse> participants

) {

    public static AthleticsDetailsResponse of(FriendlyAthletics friendlyAthletics) {
        return new AthleticsDetailsResponse(
                null,
                friendlyAthletics.getAthleticsParticipants().stream()
                        .filter(athleticsParticipant -> athleticsParticipant.getGameTeamSide().equals(GameTeamSide.NONE))
                        .map(UserInTeamDetailsResponse::of)
                        .toList()
        );
    }

    public static AthleticsDetailsResponse of(RankAthletics rankAthletics, GameTeamSide gameTeamSide) {
        Map<Team, List<TeamAthleticsParticipant>> hashMap = rankAthletics.getAthleticsParticipants().stream()
                .filter(athleticsParticipant -> athleticsParticipant instanceof TeamAthleticsParticipant &&
                        athleticsParticipant.getGameTeamSide().equals(gameTeamSide))
                .map(athleticsParticipant -> (TeamAthleticsParticipant) athleticsParticipant)
                .collect(Collectors.groupingBy(TeamAthleticsParticipant::getTeam));

        return new AthleticsDetailsResponse(
                hashMap.entrySet()
                        .stream()
                        .map(SubTeamResponse::of)
                        .toList(),
                rankAthletics.getAthleticsParticipants().stream()
                        .filter(athleticsParticipant -> athleticsParticipant instanceof SoloAthleticsParticipant &&
                                athleticsParticipant.getGameTeamSide().equals(gameTeamSide)
                        )
                        .map(UserInTeamDetailsResponse::of)
                        .toList()
        );
    }

    private static Map<Long, TeamAthleticsParticipant> getTeams(List<TeamAthleticsParticipant> teamAthleticsParticipants) {
        Map<Long, TeamAthleticsParticipant> hashMap = new HashMap<>();
        teamAthleticsParticipants.forEach(teamAthleticsParticipant -> hashMap.put(teamAthleticsParticipant.getTeam().getId(),
                teamAthleticsParticipant));
        return hashMap;
    }
}
