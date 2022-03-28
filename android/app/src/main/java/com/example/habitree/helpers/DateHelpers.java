package com.example.habitree.helpers;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previous;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateHelpers {
    public static final int DAYS_IN_WEEK = 7;
    public static final long WEEK = 7 * 24 * 3600 * 1000;

    public static boolean dateOccuredInWeek(Date startOfWeek, Date date) {
        return date.after(startOfWeek) && date.before(new Date(startOfWeek.getTime() + WEEK));
    }


    public static Date startOfCurrentWeek() {
        final LocalDate today = LocalDate.now();
        ZoneId defaultZoneId = ZoneId.systemDefault();

        if(today.atStartOfDay().getDayOfWeek() == SUNDAY) {
            return Date.from(today.atStartOfDay(defaultZoneId).toInstant());
        }
        final LocalDate thisPastSunday = today.with(previous(SUNDAY));

        return Date.from(thisPastSunday.atStartOfDay(defaultZoneId).toInstant());
    }
}
