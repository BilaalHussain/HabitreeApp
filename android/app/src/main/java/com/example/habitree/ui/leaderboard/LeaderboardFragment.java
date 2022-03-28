package com.example.habitree.ui.leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.R;
import com.example.habitree.api.FirestoreAPI;
import com.example.habitree.api.HabitApi;
import com.example.habitree.listener.PersonTapped;
import com.example.habitree.model.PersonModel;
import com.example.habitree.model.TreeModel;
import com.example.habitree.presenter.LeaderboardPresenter;
import com.example.habitree.ui.follow.FollowFragment;
import com.example.habitree.ui.tree.TreeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";
    ArrayList<LeaderboardFriendModel> leaderboardlist = new ArrayList<>();
    private final FirestoreAPI firestoreAPI = new FirestoreAPI(this.getContext());

    private void setUpLeaderboardFriends() {
        //connect to the backend later - for now hardcode
//        List<PersonModel> followees = firestoreAPI.getFolloweeScores(UID); TODO: Fix getFollower method
        List<PersonModel> followees = new ArrayList<PersonModel>();
        followees.add(new PersonModel(new ArrayList<Float>(Arrays.asList(0f, 2.0f, 3.1f, 4.1f, 4.1f)), "Roberto", UUID.randomUUID().toString()));
        followees.add(new PersonModel(new ArrayList<Float>(Arrays.asList(1.0f, 2.0f, 3.1f, 4.1f, 4.1f)), "Meimei", UUID.randomUUID().toString()));
        followees.add(new PersonModel(new ArrayList<Float>(Arrays.asList(1.0f, 6.0f, 3.1f, 4.1f, 3.1f)), "Xiaoli", UUID.randomUUID().toString()));
        followees.add(new PersonModel(new ArrayList<Float>(Arrays.asList(1.0f, 2.0f, 3.1f, 4.1f, 0.1f)), "Sam", UUID.randomUUID().toString()));
        followees.add(new PersonModel(new ArrayList<Float>(Arrays.asList(1.0f, 2.0f, 3.1f, 4.1f, 0.1f)), "Sam", UUID.randomUUID().toString()));
        followees.add(new PersonModel(new ArrayList<Float>(Arrays.asList(1.0f, 2.0f, 3.1f, 4.1f, 0.1f)), "Sam", UUID.randomUUID().toString()));
        followees.add(new PersonModel(new ArrayList<Float>(Arrays.asList(1.0f, 2.0f, 3.1f, 4.1f, 0.1f)), "Sam", UUID.randomUUID().toString()));

        for (PersonModel person : followees) {
            leaderboardlist.add(new LeaderboardFriendModel(person.getName(), person.getScore()));
        }

        Collections.sort(leaderboardlist);
        Collections.reverse(leaderboardlist);
    }

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
                view ->
                {
                    leaderboardPresenter.saveScore(leaderboardPresenter.getScores(),
                            LeaderboardPresenter.shouldGiveBonus());
                    if (LeaderboardPresenter.shouldGiveBonus()) {
                        Toast.makeText(getContext(), "You uploaded your tree early! You got bonus progress!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Uploaded score", Toast.LENGTH_SHORT).show();
                    }
                });

        leaderboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        followUserButton.setOnClickListener(event -> {
            replaceFragment(FollowFragment.newInstance());
        });

        RecyclerView recyclerView = root.findViewById(R.id.LRecyclerView);

        setUpLeaderboardFriends();

        LeaderboardAdapter adapter = new LeaderboardAdapter(getContext(), leaderboardlist);
        adapter.setEventListener(event -> {
            PersonTapped personTappedEvent = (PersonTapped) event;
            TreeModel tree = new TreeModel(personTappedEvent.person.friendName + "'s Tree", personTappedEvent.person.friendScore, false);
            replaceFragment(TreeFragment.newInstance(tree));
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // SHARE Button
        final Button share = root.findViewById(R.id.leaderboard_share);
        share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_leaderboard) + uid);
            startActivity(Intent.createChooser(shareIntent, "Share your position!"));
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