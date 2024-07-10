package com.example.playgroundmanage.util;

import com.example.playgroundmanage.althlectis.dto.GameTimeDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.playgroundmanage.util.Util.localDateToYearMonthDateTimeString;


@RequiredArgsConstructor
@Component
public final class GameValidation {



    public static void validateStartTimeAfterPresent(GameTimeDto gameTimeDto) {
        if (gameTimeDto.getStartDateTime().isBefore(LocalDateTime.now().plusMinutes(1))) {
            throw new RuntimeException("您不能提前于当前时间 + 1 分钟开始 。 当前时间 : " + localDateToYearMonthDateTimeString(LocalDateTime.now()));
        }
    }




}
