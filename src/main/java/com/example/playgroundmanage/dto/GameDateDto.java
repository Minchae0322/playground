package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.MyDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Getter
@RequiredArgsConstructor
public class GameDateDto {

    private ZonedDateTime gameStartDateTime;

    private Integer runningTime;

    @Builder
    public GameDateDto(ZonedDateTime gameStartDateTime, Integer runningTime) {
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
    }

    public MyDateTime getMyDateTime() {
        return new MyDateTime(gameStartDateTime);
    }
}
