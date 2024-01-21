package com.example.playgroundmanage.request.vo.impl;

import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.game.vo.User;
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
    public TeamJoinRequest(Long id, User user, Team team, User leader, String introduction, LocalDateTime requestTime) {
        super(id, user, team, leader, introduction, requestTime);
    }
}
