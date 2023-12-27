package com.example.playgroundmanage.date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


@Getter
public class MyDateTime {

    private final static ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");
    private final LocalDateTime localDateTime;

    public static MyDateTime getMyDateTime(ZonedDateTime zonedDateTime) {
        return new MyDateTime(zonedDateTime);
    }

    public MyDateTime(ZonedDateTime zonedDateTime) {
        ZonedDateTime localTime = zonedDateTime.withZoneSameInstant(DEFAULT_ZONE_ID);
        this.localDateTime = localTime.toLocalDateTime();
    }

}
