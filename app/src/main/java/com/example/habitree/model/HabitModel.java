package com.example.habitree.model;

// idk a better way to make models in java :///
// contains all the information needed for a habit.
public class HabitModel {
    public String name;
    public Integer current;
    public Integer goal;

    public HabitModel(
            String name,
            Integer current,
            Integer goal
    ) {
        this.name = name;
        this.current = current;
        this.goal = goal;
    }
}
