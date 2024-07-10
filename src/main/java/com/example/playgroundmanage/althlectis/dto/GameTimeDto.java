package com.example.playgroundmanage.althlectis.dto;

import com.example.playgroundmanage.date.DateTime;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class GameTimeDto {
    private LocalDateTime startDateTime;

    private Integer runningTime;

    @Builder
    public GameTimeDto(LocalDateTime startDateTime, Integer runningTime) {
        this.startDateTime = startDateTime;
        this.runningTime = runningTime;
    }
}
