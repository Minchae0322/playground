package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.dto.response.PendingTeamRequest;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamRequestInfoDto {

    private Long requestId;

    private String introduction;

    private Team team;

    private User user;

    private User leader;

    private LocalDateTime requestTime;

    @Builder
    public TeamRequestInfoDto(Long requestId, String introduction, Team team, User user, User leader, LocalDateTime requestTime) {
        this.requestId = requestId;
        this.introduction = introduction;
        this.team = team;
        this.user = user;
        this.leader = leader;
        this.requestTime = requestTime;
    }

    public PendingTeamRequest ToPendingTeamRequest() {
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
