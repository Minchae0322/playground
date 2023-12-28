package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.date.DateTime;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GameTimeDto {
    private DateTime startDateTime;

    private Integer runningTime;

    @Builder
    public GameTimeDto(DateTime startDateTime, Integer runningTime) {
        this.startDateTime = startDateTime;
        this.runningTime = runningTime;
    }
}
