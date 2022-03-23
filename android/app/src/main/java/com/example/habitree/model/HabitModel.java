package com.example.habitree.model;

import com.example.habitree.autocomplete.AbstractAutocompleteDelegate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

// idk a better way to make models in java :///
// contains all the information needed for a habit.
public class HabitModel implements Serializable {
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
    public Target target; // Target is an abstract class. Either BinaryTarget or IntegerTarget. Handles habit progress
    public List<TagModel> tags;

    public HabitModel(
            UUID id,
            String name,
            Category category,
            Target target,
            List<TagModel> tags
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.target = target;
    }
}
