package com.example.playgroundmanage.request.vo.impl;

import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.RequestState;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamJoinRequest extends TeamRequest {

    @Builder
    public TeamJoinRequest(Long id, RequestState requestState, User user, User leader, Team team, String introduction) {
        super(id, requestState, user, leader, team, introduction);
    }

    public static TeamJoinRequest of(User user, User leader, Team team, String introduce) {
        return TeamJoinRequest.builder()
                .user(user)
                .requestState(RequestState.PENDING)
                .leader(leader)
                .team(team)
                .introduction(introduce)
                .build();
    }

}
