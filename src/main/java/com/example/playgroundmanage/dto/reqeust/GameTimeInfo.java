package com.example.playgroundmanage.dto.reqeust;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;




@Getter
@RequiredArgsConstructor
public class GameTimeInfo {

    private ZonedDateTime gameStartDateTime;

    private Integer runningTime;


    @Builder
    public GameTimeInfo(ZonedDateTime gameStartDateTime, Integer runningTime) {
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
    }

    public GameTimeDto toGameTimeDto() {
        return GameTimeDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(gameStartDateTime))
                .runningTime(runningTime)
                .build();
    }


}
