package com.example.habitree.model;

import android.annotation.SuppressLint;

import com.example.habitree.geofence.GeofenceInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WeeklyHabit extends HabitModel {
    public WeeklyHabit(UUID id, String name, Category category, List<TagModel> tags, int target, GeofenceInfo geofenceInfo) {
        super(id, name, category, tags, geofenceInfo);
        this.target = target;
    }

    public WeeklyHabit(UUID id, String name, Category category, List<Date> daysHabitCompleted, List<TagModel> tags, int target, GeofenceInfo geofenceInfo) {
        super(id, name, category, daysHabitCompleted, tags, geofenceInfo);
        this.target = target;
    }

    public int target;

    @Override
    float getTreeWeeklyScore(Date startOfWeek) {
        return (float) this.getCompletionStatus(startOfWeek) / (float) target;
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
    public boolean isToDo(Date startOfWeek) {
        Date currTime = Calendar.getInstance().getTime();
        int numDaysHabitCompletedInLastWeek = (int) daysHabitCompleted.stream().filter(day ->
                day.after(startOfWeek) && day.before(currTime)
        ).count();
        return numDaysHabitCompletedInLastWeek < target;
    }

    @Override
    public String getReadableStatusString(Date startOfWeek) {
        return String.format("%s/%s", getCompletionStatus(startOfWeek), target);
    }
}
