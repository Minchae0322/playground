package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.login.vo.User;
import lombok.Data;


@Data
public class RequestDto {

    private User user;
    private MyDateTime requestTime;

    public RequestDto(User user, MyDateTime requestTime) {
        this.user = user;
        this.requestTime = requestTime;
    }
}
