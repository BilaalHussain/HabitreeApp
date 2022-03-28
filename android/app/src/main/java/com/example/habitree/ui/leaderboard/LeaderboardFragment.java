package com.example.habitree.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.habitree.R;
import com.example.habitree.api.FirestoreAPI;
import com.example.habitree.api.HabitApi;
import com.example.habitree.presenter.LeaderboardPresenter;
import com.example.habitree.ui.follow.FollowFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";
    private LeaderboardViewModel leaderboardViewModel;
    private LeaderboardPresenter leaderboardPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        final Button followUserButton = root.findViewById(R.id.follow_user_button);

        leaderboardPresenter = new LeaderboardPresenter(
                new FirestoreAPI(requireContext()),
                new HabitApi(requireContext()),
                FirebaseAuth.getInstance().getCurrentUser().getUid()
                );

        root.findViewById(R.id.upload_score_button).setOnClickListener(
                view -> {

                    leaderboardPresenter.saveScore(leaderboardPresenter.getScores(),
                                                    leaderboardPresenter.shouldGiveBonus());
                }
        );

        leaderboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        followUserButton.setOnClickListener(event -> {
            replaceFragment(FollowFragment.newInstance());
        });

        return root;
    }

    private void replaceFragment(Fragment f) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction t = fm.beginTransaction().replace(R.id.nav_host_fragment, f);
        t.addToBackStack(null);
        t.commit();
    }
}