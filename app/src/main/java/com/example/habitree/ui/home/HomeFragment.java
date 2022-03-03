package com.example.habitree.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.R;
import com.example.habitree.listener.Event;
import com.example.habitree.listener.EventListener;
import com.example.habitree.listener.HabitTapped;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.HomeModel;
import com.example.habitree.presenter.HomePresenter;
import com.example.habitree.ui.editing.EditHabitFragment;
import com.example.habitree.ui.HabitAdapter;
import com.example.habitree.view.AbstractView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class HomeFragment extends Fragment implements AbstractView<HomePresenter> {

    private HomeModel homeModel;
    private HomePresenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        final TextView title = root.findViewById(R.id.todays_habit_title);
        final RecyclerView habitList = root.findViewById(R.id.habit_list);

        title.setText(R.string.todays_habits);

        setPresenter(new HomePresenter(this));

        // call methods to set up presenter -
        homeModel = presenter.onViewCreated();

        // update the view
        // can set on click methods and stuff here
        HabitModel habit = homeModel.habits.get(0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        HabitAdapter habitAdapter = new HabitAdapter();
        habitAdapter.setEventListener(event -> {
//            UUID habitId = ((HabitTapped) event).id;
//            Integer index = IntStream.range(0, homeModel.habits.size())
//                    .filter(x -> habitId.equals(homeModel.habits.get(x).id))
//                    .findFirst();
//            replaceFragment(EditHabitFragment.newInstance(homeModel.habits.));
            replaceFragment(EditHabitFragment.newInstance(homeModel.habits.get(0)));
            // TODO edit this event listener to instead navigate to the habit edit page
//            List<HabitModel> newHabits = presenter.markHabitAsComplete(habit);
//            habitAdapter.setCurrentHabits(newHabits);
        });
        habitList.setAdapter(habitAdapter);
        habitList.setLayoutManager(linearLayoutManager);

        habitAdapter.setCurrentHabits(homeModel.habits);

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

    private void replaceFragment(Fragment f) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction t = fm.beginTransaction().replace(R.id.nav_host_fragment,f);
        t.addToBackStack(null);
        t.commit();
    }
}