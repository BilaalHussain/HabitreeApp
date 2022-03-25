package com.example.habitree.model;

import com.example.habitree.geofence.GeofenceInfo;

import java.io.Serializable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// contains all the information needed for a habit.
public abstract class HabitModel implements Serializable {
    public enum Category {
        WORK("Work", 0),
        ACADEMIC("Academic", 1),
        FITNESS("Fitness", 2),
        CREATIVE("Creative", 3),
        SELF_HELP("Self Help", 4);

        private String displayName;
        private int index;

        Category(String displayName, int index) { this.displayName = displayName; this.index = index; }
        public String toString() { return displayName; }
        public int getIndex() { return index; }
    }

    public static Category stringToCategory(String str) {
        List<Category> matches = Stream.of(HabitModel.Category.values())
                .filter(category -> {
                    return category.toString().equals(str);
                }).collect(Collectors.toList());
        if (matches.size() != 1) return Category.WORK;
        return matches.get(0);
    }

    public UUID id;
    public String name;
    public Category category;
    public List<Date> daysHabitCompleted;
    public List<TagModel> tags;
    public GeofenceInfo geofenceInfo;
    public HabitModel(
            UUID id,
            String name,
            Category category,
            List<TagModel> tags,
            GeofenceInfo geofenceInfo
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tags = tags;
        this.daysHabitCompleted = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public HabitModel(
            UUID id,
            String name,
            Category category,
            List<Date> daysHabitCompleted,
            List<TagModel> tags,
            GeofenceInfo geofenceInfo
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tags = tags;
        this.daysHabitCompleted = daysHabitCompleted;
    }

    public void complete() {
        // adds the current day to the completed days
        daysHabitCompleted.add(Calendar.getInstance().getTime());
    }

    public void uncomplete() {
        // removes the current day from the days habit completed
        Date currTime = Calendar.getInstance().getTime();
        Instant instant1 = currTime.toInstant().truncatedTo(ChronoUnit.DAYS);
        daysHabitCompleted.removeIf(
                date -> instant1.equals(date.toInstant().truncatedTo(ChronoUnit.DAYS))
        );
    }

    public boolean isToDoToday() {
        Date currTime = Calendar.getInstance().getTime();
        Instant instant1 = currTime.toInstant().truncatedTo(ChronoUnit.DAYS);
        return !daysHabitCompleted.stream().anyMatch(day ->
                instant1.equals(day.toInstant().truncatedTo(ChronoUnit.DAYS))
        );
    }

    // getCompletionStatus/target amount (7 if daily, set if Weekly)
    abstract float getTreeWeeklyScore(Date startOfWeek);
    // how many days have they completed this habit since startOfWeek
    abstract int getCompletionStatus(Date startOfWeek);
    // does the user still have to do this habit this week
    abstract public boolean isToDo(Date startOfWeek);

    abstract public String getReadableStatusString(Date startOfWeek);
}
