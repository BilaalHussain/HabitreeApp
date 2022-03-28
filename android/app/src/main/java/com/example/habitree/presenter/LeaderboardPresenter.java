package com.example.habitree.presenter;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import android.view.View;

import com.example.habitree.api.FirestoreAPI;
import com.example.habitree.api.HabitApi;
import com.example.habitree.helpers.DateHelpers;
import com.example.habitree.model.ScoreModel;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class LeaderboardPresenter implements AbstractPresenter {

    View view;
    FirestoreAPI firestoreAPI;
    HabitApi habitApi;
    String firebaseUuid;

    @Override
    public void onDestroy() {
        this.view = null;
    }

    public LeaderboardPresenter(FirestoreAPI fApi, HabitApi hApi, String uuid) {
        firestoreAPI = fApi;
        habitApi = hApi;
        firebaseUuid = uuid;
    }

    public ScoreModel getScores() {
        return new ScoreModel(
                habitApi.getAllHabits(),
                DateHelpers.startOfCurrentWeek(),
                firebaseUuid);
    }

    public void saveScore(ScoreModel scoreModel) {
        firestoreAPI.saveScore(firebaseUuid, scoreModel.getScore());
    }
}
