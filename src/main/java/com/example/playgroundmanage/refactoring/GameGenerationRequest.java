package com.example.playgroundmanage.refactoring;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GameGenerationRequest(
        @NotBlank(message = "Input Playground ID")
        Long playgroundId,
        @NotBlank(message = "Input Game Type")
        GameType gameType,
        @NotBlank(message = "Input Game Name")
        @Size(max = 12, message = "Max game title 12 length")
        String gameName,
        @NotBlank(message = "Input Game Start Time")
        DateTime startDateTime,
        @NotBlank(message = "Input Sports Event")
        SportsEvent sportsEvent,
        @NotBlank(message = "Input Running Time")
        Integer runningTime) {

    public GameTimeDto toGameTimeDto() {
        return GameTimeDto.builder()
                .runningTime(runningTime)
                .startDateTime(startDateTime)
                .build();
    }

    public Athletics toEntity() {
        return RankAthletics.builder()
                .gameName(gameName)
                .gameType(GameType.COMPETITION)
                .gameStartDateTime(startDateTime.getLocalDateTime())
                .sportsEvent(sportsEvent)
                .runningTime(runningTime)
                .build();
    }

}
