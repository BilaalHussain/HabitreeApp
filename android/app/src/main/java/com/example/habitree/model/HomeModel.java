package com.example.habitree.model;

import java.util.List;

// this contains all the data needed for the home page
public class HomeModel {
    public List<HabitModel> habits;

    public HomeModel(List<HabitModel> habits) {
         this.habits = habits;
    }
}
