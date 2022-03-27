package com.example.habitree.ui.leaderboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.api.FirestoreAPI;
import com.example.habitree.model.PersonModel;
import com.example.habitree.ui.follow.FollowFragment;

import com.example.habitree.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    ArrayList<LeaderboardFriendModel> leaderboardlist = new ArrayList<>();
    private final FirestoreAPI firestoreAPI = new FirestoreAPI(this.getContext());

    private void setUpLeaderboardFriends() {
        //connect to the backend later - for now hardcode
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<PersonModel> followees = firestoreAPI.getFolloweeScores(UID);


        for (PersonModel person: followees) {
            List<Float> personScores = person.getScores();
            Float scoreTotal = 0.0f;
            for (Float score: personScores) {
                scoreTotal = Float.sum(score,scoreTotal);
            }
            leaderboardlist.add(new LeaderboardFriendModel(person.getName(),scoreTotal));
        }

        Collections.sort(leaderboardlist);
    }

    private LeaderboardViewModel leaderboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        final Button followUserButton = root.findViewById(R.id.follow_user_button);

        leaderboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        followUserButton.setOnClickListener(event -> {
            replaceFragment(FollowFragment.newInstance());
        });

        RecyclerView recyclerView = root.findViewById(R.id.LRecyclerView);

        setUpLeaderboardFriends();

        LeaderboardAdapter adapter = new LeaderboardAdapter(getContext(), leaderboardlist);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private void replaceFragment(Fragment f) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction t = fm.beginTransaction().replace(R.id.nav_host_fragment,f);
        t.addToBackStack(null);
        t.commit();
    }

}