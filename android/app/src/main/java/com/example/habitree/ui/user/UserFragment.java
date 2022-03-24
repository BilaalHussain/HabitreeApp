package com.example.habitree.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.habitree.R;
import com.example.habitree.api.HabitApi;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.ScoreModel;
import com.example.habitree.model.TreeModel;
import com.example.habitree.ui.tree.TreeFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        List<HabitModel> habits = (new HabitApi(getContext())).getAllHabits();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();
        Log.d("JAMIE", startOfWeek.toString());
        ScoreModel score = new ScoreModel(habits, startOfWeek);
        TreeModel tree = new TreeModel("Good morning!", score.getTreeUri());
        replaceFragment(TreeFragment.newInstance(tree));

        return root;
    }

    private void replaceFragment(Fragment f) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction t = fm.beginTransaction().replace(R.id.nav_host_fragment, f);
        t.addToBackStack(null);
        t.commit();
    }
}