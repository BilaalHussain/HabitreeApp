package com.example.habitree.model;

import com.example.habitree.autocomplete.AbstractAutocompleteDelegate;
import android.annotation.SuppressLint;
import android.os.Build;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// idk a better way to make models in java :///
// contains all the information needed for a habit.
public abstract class HabitModel implements Serializable {
    public enum Category {
        WORK("Work"),
        ACADEMIC("Academic"),
        FITNESS("Fitness"),
        CREATIVE("Creative"),
        SELF_HELP("Self Help");

        private String displayName;

        Category(String displayName) { this.displayName = displayName; }
        public String toString() { return displayName; }
    }

    public UUID id;
    public String name;
    public Category category;
    public List<Date> daysHabitCompleted;
    public List<TagModel> tags;

    public HabitModel(
            UUID id,
            String name,
            Category category,
            List<TagModel> tags
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tags = tags;
    }

    public void complete() {
        // adds the current day to the completed days
        daysHabitCompleted.add(Calendar.getInstance().getTime());
    }

    // getCompletionStatus/target amount (7 if daily, set if Weekly)
    abstract float getTreeWeeklyScore(Date startOfWeek);
    // how many days have they completed this habit since startOfWeek
    abstract int getCompletionStatus(Date startOfWeek);
    // does the user still have to do this habit this week
    abstract boolean isToDo(Date startOfWeek);
}
