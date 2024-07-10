package com.example.playgroundmanage.althlectis.dto.request;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.althlectis.dto.GameTimeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

public record GameGenerationRequest(
        @NotBlank(message = "Input Playground ID")
        Long playgroundId,
        @NotBlank(message = "Input Game Type")
        String gameType,
        @NotBlank(message = "Input Game Name")
        @Size(max = 12, message = "Max game title 12 length")
        String gameName,
        @NotBlank(message = "Input Game Start Time")
        ZonedDateTime gameStartDateTime,
        @NotBlank(message = "Input Sports Event")
        String sportsEvent,
        @NotBlank(message = "Input Running Time")
        Integer runningTime) {

    public GameTimeDto toGameTimeDto() {
        return GameTimeDto.builder()
                .runningTime(runningTime)
                .startDateTime(MyDateTime.initMyDateTime(gameStartDateTime).getLocalDateTime())
                .build();
    }



}
