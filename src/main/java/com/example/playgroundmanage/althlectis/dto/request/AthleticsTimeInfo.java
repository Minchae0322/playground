package com.example.playgroundmanage.althlectis.dto.request;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;




public record AthleticsTimeInfo(

        @NotBlank(message = "The game start date and time must not be null.")
        @FutureOrPresent(message = "The game start date and time must be in the future or present.")
        ZonedDateTime gameStartDateTime,

        @NotBlank(message = "The game running time must not be null.")
        @Min(value = 1, message = "Running time must be at least 1 minute.")
        Integer runningTime


) {
    public static GameTimeDto toGameTimeDto(AthleticsTimeInfo athleticsTimeInfo) {
        return GameTimeDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(athleticsTimeInfo.gameStartDateTime).getLocalDateTime())
                .runningTime(athleticsTimeInfo.runningTime)
                .build();
    }
}

