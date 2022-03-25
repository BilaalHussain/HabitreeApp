package com.example.habitree.helpers;

import java.util.Date;

public class DateHelpers {
    public static final int DAYS_IN_WEEK = 7;
    public static final long WEEK = 7 * 24 * 3600 * 1000;

    public static boolean dateOccuredInWeek(Date startOfWeek, Date date) {
        return date.after(startOfWeek) && date.before(new Date(startOfWeek.getTime() + WEEK));
    }
}
