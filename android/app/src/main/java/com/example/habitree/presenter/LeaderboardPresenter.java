package com.example.habitree.presenter;

import android.view.View;

public class LeaderboardPresenter implements  AbstractPresenter{

    View view;
    @Override
    public void onDestroy() {
        this.view = null;
    }
}
