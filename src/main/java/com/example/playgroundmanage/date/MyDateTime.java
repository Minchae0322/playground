package com.example.playgroundmanage.date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.example.playgroundmanage.util.Util.localDateToYearMonthDateTimeString;


@Getter
public class MyDateTime implements DateTime {

    private final static ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");
    private final ZonedDateTime zonedDateTime;

    public static MyDateTime initMyDateTime(ZonedDateTime zonedDateTime) {
        return new MyDateTime(zonedDateTime);
    }

    private MyDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    @Override
    public LocalDateTime getLocalDateTime() {
        ZonedDateTime localTime = zonedDateTime.withZoneSameInstant(DEFAULT_ZONE_ID);
        return localTime.toLocalDateTime().withSecond(0).withNano(0);
    }

    @Override
    public String toStringValue() {
        ZonedDateTime localTime = zonedDateTime.withZoneSameInstant(DEFAULT_ZONE_ID);
        return localDateToYearMonthDateTimeString(localTime.toLocalDateTime());
    }


}
