package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.date.MyDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
public class GameTimeDto {
    private MyDateTime myDateTime;

    private Integer runningTime;


    @Builder
    public GameTimeDto(MyDateTime myDateTime, Integer runningTime) {
        this.myDateTime = myDateTime;
        this.runningTime = runningTime;
    }
}
