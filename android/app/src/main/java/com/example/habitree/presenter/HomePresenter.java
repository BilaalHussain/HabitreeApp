package com.example.habitree.presenter;

import android.content.Context;

import com.example.habitree.api.HabitApi;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.HomeModel;
import com.example.habitree.ui.home.HomeFragment;

import java.util.List;

// handles logic and calls to backend and sends data to the view
public class HomePresenter implements AbstractPresenter {
    HomeFragment view;
    HabitApi habitApi;
    public HomePresenter(HomeFragment view, HabitApi habitApi) {
        this.view = view;
        this.habitApi = habitApi;
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    public HomeModel onViewCreated() {
        List<HabitModel> habits = habitApi.getAllHabits();
        return new HomeModel(habits);
    }

    public List<HabitModel> markHabitAsComplete(HabitModel habit) {
        habitApi.updateHabit(habit);
        return habitApi.getAllHabits();
    }
}
