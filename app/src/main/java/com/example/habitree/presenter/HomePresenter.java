package com.example.habitree.presenter;

import com.example.habitree.model.HabitModel;
import com.example.habitree.model.HomeModel;
import com.example.habitree.ui.home.HomeFragment;

import java.util.ArrayList;
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

    // returns dummy data for now
    public HomeModel onViewCreated() {
        List<HabitModel> habits = new ArrayList<>();
        habits.add(new HabitModel( "name", 0, 10));
        return new HomeModel(habits);
        // do stuff like memory calls
    }
}
