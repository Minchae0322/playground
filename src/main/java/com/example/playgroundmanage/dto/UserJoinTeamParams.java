package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserJoinTeamParams {
    private Long subTeamId;

    private User user;


    @Builder
    public UserJoinTeamParams(Long subTeamId, User user) {
        this.subTeamId = subTeamId;
        this.user = user;
    }
}
