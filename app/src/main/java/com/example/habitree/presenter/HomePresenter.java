package com.example.habitree.presenter;

import com.example.habitree.api.HabitApi;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.HomeModel;
import com.example.habitree.ui.home.HomeFragment;

import java.util.List;

// handles logic and calls to backend and sends data to the view
public class HomePresenter implements AbstractPresenter {
    HomeFragment view;
    public HomePresenter(HomeFragment view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    public HomeModel onViewCreated() {
        List<HabitModel> habits = HabitApi.getAllHabits(view.getContext());
        return new HomeModel(habits);
    }

    public void markHabitAsComplete(HabitModel habit) {
        habit.current += 1;
        HabitApi.updateHabit(view.getContext(), habit);
    }
}
