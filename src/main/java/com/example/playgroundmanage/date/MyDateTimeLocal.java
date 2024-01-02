package com.example.playgroundmanage.date;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.example.playgroundmanage.util.Util.localDateToYearMonthDateTimeString;

@Getter
public class MyDateTimeLocal implements DateTime {

    private final LocalDateTime localDateTime;

    public static MyDateTimeLocal initMyDateTime(LocalDateTime localDateTime) {
        return new MyDateTimeLocal(localDateTime);
    }

    private MyDateTimeLocal(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime getLocalDateTime() {
        return localDateTime.withSecond(0).withNano(0);
    }

    @Override
    public String toStringValue() {

        return localDateToYearMonthDateTimeString(localDateTime);
    }
}
