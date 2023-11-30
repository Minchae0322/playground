package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubTeamRegistrationParams {

    private Long matchTeamId;
    private Long teamId;
    private User user;



    @Builder
    public SubTeamRegistrationParams(Long matchTeamId, Long teamId, User user) {
        this.matchTeamId = matchTeamId;
        this.teamId = teamId;
        this.user = user;
    }
}
