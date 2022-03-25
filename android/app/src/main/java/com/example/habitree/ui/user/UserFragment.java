package com.example.habitree.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.habitree.R;
import com.example.habitree.api.HabitApi;
import com.example.habitree.helpers.DateHelpers;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.ScoreModel;
import com.example.habitree.model.TreeModel;
import com.example.habitree.ui.tree.TreeFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserFragment extends Fragment {

    private Date startOfCurrentWeek, selectedWeek;
    private Button prevBtn, nextBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        startOfCurrentWeek = cal.getTime();

        prevBtn = root.findViewById(R.id.previous_week_btn);
        nextBtn = root.findViewById(R.id.next_week_btn);

        prevBtn.setOnClickListener(v -> {
            updateSelectedWeek(new Date(selectedWeek.getTime() - DateHelpers.WEEK));
        });

        nextBtn.setOnClickListener(v -> {
            updateSelectedWeek(new Date(selectedWeek.getTime() + DateHelpers.WEEK));
        });

        return root;
    }

    private void updateSelectedWeek(Date newWeek) {
        if (newWeek.after(startOfCurrentWeek)) {
            return;
        }

        selectedWeek = newWeek;
        renderTree(selectedWeek);

        nextBtn.setEnabled(selectedWeek != startOfCurrentWeek);
    }

    private void renderTree(Date startOfWeek) {
        List<HabitModel> habits = (new HabitApi(getContext())).getAllHabits();

        ScoreModel score = new ScoreModel(habits, startOfWeek);
        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
        TreeModel tree = new TreeModel("Week of " + df.format(startOfWeek), score);
        replaceFragment(TreeFragment.newInstance(tree));
    }

    private void replaceFragment(Fragment f) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction t = fm.beginTransaction().replace(R.id.tree_view, f);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSelectedWeek(startOfCurrentWeek);
    }
}