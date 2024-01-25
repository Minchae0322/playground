package com.example.playgroundmanage.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormat {

    public static String dateFormatYYYYMMDDHHMM(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String dateFormatYYYYMMDD(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalDateTime dateFormatWith0Second(LocalDateTime localDateTime) {
        return localDateTime.withSecond(0).withNano(0);
    }
}
