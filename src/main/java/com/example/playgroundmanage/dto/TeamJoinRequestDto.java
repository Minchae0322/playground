package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.login.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamJoinRequestDto extends RequestDto {

    private Long teamId;

    private String introduction;

    private String type;

    private String sportsEvent;

    @Builder
    public TeamJoinRequestDto(User user, MyDateTime requestTime, Long teamId, String introduction, String type, String sportsEvent) {
        super(user, requestTime);
        this.teamId = teamId;
        this.introduction = introduction;
        this.type = type;
        this.sportsEvent = sportsEvent;
    }
}
