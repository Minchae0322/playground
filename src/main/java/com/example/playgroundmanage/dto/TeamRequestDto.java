package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequestDto extends RequestDto{

    private Long teamId;

    private String introduction;

    @Builder
    public TeamRequestDto(User user, MyDateTime requestTime, Long teamId, String introduction) {
        super(user, requestTime);
        this.teamId = teamId;
        this.introduction = introduction;
    }
}
