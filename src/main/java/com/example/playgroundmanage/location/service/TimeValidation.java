package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.exception.TimeOverlappingException;
import com.example.playgroundmanage.game.dto.GameTimeDto;

import com.example.playgroundmanage.util.DateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.Util.localDateToYearMonthDateTimeString;


@Component
@RequiredArgsConstructor
public class TimeValidation {

    private final DateFormat dateFormat;
    public boolean isAfterFromNow(LocalDateTime gameStartDateTime) {
        return gameStartDateTime.isAfter(LocalDateTime.now());
    }

    public boolean isAthleticsOnDate(Athletics athletics, LocalDateTime targetDate) {
        return dateFormat.dateFormatYYYYMMDD(athletics.getGameStartDateTime()).equals(dateFormat.dateFormatYYYYMMDD(targetDate));
    }

    public void validateOverlappingGames(List<Athletics> athletics, GameTimeDto gameTimeDto) {
        boolean isOverlapping = athletics.stream()
                .anyMatch(athletic -> isDateTimeRangeOverlapping(athletic, gameTimeDto.getStartDateTime(), gameTimeDto.getRunningTime()));

        if (isOverlapping) {
            throw new TimeOverlappingException(localDateToYearMonthDateTimeString(LocalDateTime.now()));
        }
    }

    private boolean isDateTimeRangeOverlapping(Athletics game, LocalDateTime localDateTime, Integer gameRunningTime) {
        LocalDateTime startDateTime = dateFormat.dateFormatWith0Second(localDateTime);
        LocalDateTime endDateTime = dateFormat.dateFormatWith0Second(localDateTime).plusMinutes(gameRunningTime);

        LocalDateTime gameStartDateTime = game.getGameStartDateTime();
        LocalDateTime gameEndDateTime = gameStartDateTime.plusMinutes(game.getRunningTime());

        return !(gameEndDateTime.isBefore(startDateTime) || gameStartDateTime.isAfter(endDateTime));
    }
}
