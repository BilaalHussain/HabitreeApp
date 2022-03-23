package com.example.habitree.model;

import android.annotation.SuppressLint;

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

    @Override
    float getTreeWeeklyScore(Date startOfWeek) {
        return (float) this.getCompletionStatus(startOfWeek)/7f;
    }

    @SuppressLint("NewApi")
    @Override
    int getCompletionStatus(Date startOfWeek) {
        Date currTime = Calendar.getInstance().getTime();
        return (int) daysHabitCompleted.stream().filter(day ->
                day.after(startOfWeek) && day.before(currTime)
        ).count();
    }

    @SuppressLint("NewApi")
    @Override
    boolean isToDo(Date startOfWeek) {
        Date currTime = Calendar.getInstance().getTime();
        Instant instant1 = currTime.toInstant()
                .truncatedTo(ChronoUnit.DAYS);
        return daysHabitCompleted.stream().anyMatch(day ->
                instant1.equals(day.toInstant().truncatedTo(ChronoUnit.DAYS))
        );
    }
}