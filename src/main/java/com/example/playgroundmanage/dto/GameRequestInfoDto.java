package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.dto.response.PendingGameRequest;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.util.DateFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameRequestInfoDto {

    private Long requestId;
    private Game game;

    private String requestType;

    private SubTeam subTeam;

    private User user;

    private Team team;

    private MatchTeamSide matchTeamSide;

    private LocalDateTime requestTime;

    @Builder
    public GameRequestInfoDto(Long requestId, Game game, String requestType, SubTeam subTeam, User user, Team team, MatchTeamSide matchTeamSide, LocalDateTime requestTime) {
        this.requestId = requestId;
        this.game = game;
        this.requestType = requestType;
        this.subTeam = subTeam;
        this.user = user;
        this.team = team;
        this.matchTeamSide = matchTeamSide;
        this.requestTime = requestTime;
    }

    public PendingGameRequest toPendingGameRequest() {
        return PendingGameRequest.builder()
                .teamName(team == null ? "" : team.getTeamName())
                .teamId(team == null ? null : team.getId())
                .gameName(game.getGameName())
                .gameId(game.getId())
                .requestTime(DateFormat.dateFormatYYYYMMDDHHMM(requestTime))
                .requestType(requestType)
                .username(user.getNickname())
                .userId(user.getId())
                .subTeamName(subTeam == null ? "" : subTeam.getTeam().getTeamName())
                .matchTeamSide(matchTeamSide.getValue())
                .build();
    }

}
