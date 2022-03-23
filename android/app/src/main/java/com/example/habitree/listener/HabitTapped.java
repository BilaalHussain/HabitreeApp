package com.example.habitree.listener;

import java.util.UUID;

public class HabitTapped implements Event {
    public UUID id;
    public HabitTapped(UUID id) {
        this.id = id;
    }
}
