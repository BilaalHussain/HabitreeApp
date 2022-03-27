package com.example.habitree.presenter;

import android.view.View;

import com.example.habitree.presenter.AbstractPresenter;

public class UserPresenter implements AbstractPresenter {

    View view;
    @Override
    public void onDestroy() {
        this.view = null;
    }
}
