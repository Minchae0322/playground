package com.example.playgroundmanage.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Util {

    public static String localDateToYearMonthDateTimeString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
