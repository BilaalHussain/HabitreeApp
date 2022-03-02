package com.example.habitree.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.habitree.R;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.HomeModel;
import com.example.habitree.presenter.HomePresenter;
import com.example.habitree.view.AbstractView;

public class HomeFragment extends Fragment implements AbstractView<HomePresenter> {

    private HomeModel homeModel;
    private HomePresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        setPresenter(new HomePresenter(this));

        // call methods to set up presenter -
        homeModel = presenter.onViewCreated();

        // update the view
        // can set on click methods and stuff here
        HabitModel habit = homeModel.habits.get(0);
        textView.setText(String.format("%s - %d/%d", habit.name, habit.current, habit.goal));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // example modification that will get persisted to disk
                presenter.markHabitAsComplete(habit);
                textView.setText(String.format("%s - %d/%d", habit.name, habit.current, habit.goal));
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void setPresenter(HomePresenter presenter) {
        this.presenter = presenter;
    }
}