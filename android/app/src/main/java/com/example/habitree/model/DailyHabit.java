package com.example.habitree.model;

import android.annotation.SuppressLint;

import com.example.habitree.helpers.DateHelpers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DailyHabit extends HabitModel {
    public DailyHabit(UUID id, String name, Category category, List<TagModel> tags) {
        super(id, name, category, tags);
    }

    public DailyHabit(UUID id, String name, Category category, List<Date> daysHabitCompleted, List<TagModel> tags) {
        super(id, name, category, daysHabitCompleted, tags);
    }

    @Override
    float getTreeWeeklyScore(Date startOfWeek) {
        return (float) this.getCompletionStatus(startOfWeek)/DateHelpers.DAYS_IN_WEEK;
    }

    @SuppressLint("NewApi")
    @Override
    int getCompletionStatus(Date startOfWeek) {
        return (int) daysHabitCompleted.stream().filter(day ->
                DateHelpers.dateOccuredInWeek(startOfWeek, day)
        ).count();
    }

    @SuppressLint("NewApi")
    @Override
    public boolean isToDo(Date startOfWeek) {
        Date currTime = Calendar.getInstance().getTime();
        Instant instant1 = currTime.toInstant()
                .truncatedTo(ChronoUnit.DAYS);
        return !daysHabitCompleted.stream().anyMatch(day ->
                instant1.equals(day.toInstant().truncatedTo(ChronoUnit.DAYS))
        );
    }

    @Override
    public String getReadableStatusString(Date startOfWeek) {
        return String.format("%s/%s", getCompletionStatus(startOfWeek), DateHelpers.DAYS_IN_WEEK);
    }
}