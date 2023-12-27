package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.response.GameTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

import static com.example.playgroundmanage.date.MyDateTime.getMyDateTime;


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
                .myDateTime(getMyDateTime(gameStartDateTime))
                .runningTime(runningTime)
                .build();
    }


}
