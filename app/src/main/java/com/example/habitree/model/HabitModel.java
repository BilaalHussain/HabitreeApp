package com.example.habitree.model;

import java.io.Serializable;
import java.util.UUID;

// idk a better way to make models in java :///
// contains all the information needed for a habit.
public class HabitModel implements Serializable {
    public UUID id;
    public String name;
    public Integer current;
    public Integer goal;

    public HabitModel(
            UUID id,
            String name,
            Integer current,
            Integer goal
    ) {
        this.id = id;
        this.name = name;
        this.current = current;
        this.goal = goal;
    }
}