package com.example.playgroundmanage.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {

    public static String dateFormatYYYYMMDDHHMM(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String dateFormatYYYYMMDD(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDateTime dateFormatWith0Second(LocalDateTime localDateTime) {
        return localDateTime.withSecond(0).withNano(0);
    }
}
