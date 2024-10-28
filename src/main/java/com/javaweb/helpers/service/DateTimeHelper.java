package com.javaweb.helpers.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {
    public static String formatEventTime(long eventTimeLong) {
        Instant instant = Instant.ofEpochMilli(eventTimeLong);
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
