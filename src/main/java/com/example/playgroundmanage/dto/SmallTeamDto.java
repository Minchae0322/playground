package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.vo.SmallTeam;
import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SmallTeamDto {

    private Long smallTeamId;

    private User user;

    @Builder
    public SmallTeamDto(Long smallTeamId, User user) {
        this.smallTeamId = smallTeamId;
        this.user = user;
    }

}
