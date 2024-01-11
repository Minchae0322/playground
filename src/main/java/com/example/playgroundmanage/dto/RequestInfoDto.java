package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.dto.response.PendingGameRequest;
import com.example.playgroundmanage.dto.response.PendingTeamRequest;
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
@Builder
public class RequestInfoDto {

    private Long requestId;
    private LocalDateTime requestTime;
    private User user;

    // GameRequestInfoDto 관련 필드
    private Game game;
    private String requestType;
    private SubTeam subTeam;
    private Team team;
    private MatchTeamSide matchTeamSide;

    // TeamRequestInfoDto 관련 필드
    private String introduction;
    private User leader;



    // 기존의 toPendingGameRequest 메서드
    public PendingGameRequest toPendingGameRequest() {
        return PendingGameRequest.builder()
                .teamName(team == null ? "" : team.getTeamName())
                .teamId(team == null ? null : team.getId())
                .gameName(game.getGameName())
                .gameId(game.getId())
                .requestId(requestId)
                .requestTime(DateFormat.dateFormatYYYYMMDDHHMM(requestTime))
                .requestType(requestType)
                .username(user.getNickname())
                .userId(user.getId())
                .subTeamName(subTeam == null ? "" : subTeam.getTeam().getTeamName())
                .matchTeamSide(matchTeamSide.getValue())
                .build();
    }

    // 기존의 ToPendingTeamRequest 메서드
    public PendingTeamRequest toPendingTeamRequest() {
        return PendingTeamRequest.builder()
                .teamName(team.getTeamName())
                .userName(user.getNickname())
                .userId(user.getId())
                .introduction(introduction)
                .requestId(requestId)
                .requestTime(requestTime)
                .build();
    }

}