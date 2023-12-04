package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubTeamRegistrationParams {

    private Long competingTeamId;
    private Long teamId;
    private User user;



    @Builder
    public SubTeamRegistrationParams(Long competingTeamId, Long teamId, User user) {
        this.competingTeamId = competingTeamId;
        this.teamId = teamId;
        this.user = user;
    }
}
