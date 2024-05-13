package com.example.playgroundmanage.refactoring;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record GameGenerationRequest(
        @NotBlank(message = "Input Playground ID")
        Long playgroundId,
        @NotBlank(message = "Input Game Type")
        String gameType,
        @NotBlank(message = "Input Game Name")
        @Size(max = 12, message = "Max game title 12 length")
        String gameName,
        @NotBlank(message = "Input Game Start Time")
        LocalDateTime gameStartDateTime,
        @NotBlank(message = "Input Sports Event")
        String sportsEvent,
        @NotBlank(message = "Input Running Time")
        Integer runningTime) {

    public GameTimeDto toGameTimeDto() {
        return GameTimeDto.builder()
                .runningTime(runningTime)
                .startDateTime(gameStartDateTime)
                .build();
    }



}
