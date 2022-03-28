package com.example.habitree.ui.follow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitree.MainActivity;
import com.example.habitree.R;
import com.example.habitree.api.FirestoreAPI;
import com.example.habitree.api.HabitApi;
import com.example.habitree.listener.DeleteTagTapped;
import com.example.habitree.listener.ToggleIsEditing;
import com.example.habitree.listener.UpdateTag;
import com.example.habitree.model.DailyHabit;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.TagModel;
import com.example.habitree.model.WeeklyHabit;
import com.example.habitree.ui.HabitAdapter;
import com.example.habitree.ui.TagAdapter;
import com.example.habitree.ui.editing.EditHabitFragment;
import com.example.habitree.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.example.habitree.api.HabitApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.habitree.ui.leaderboard.LeaderboardFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditHabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowFragment extends Fragment {
    private final FirestoreAPI firestoreAPIApi = new FirestoreAPI(this.getContext());

    private FollowFragment() {
    }
    public static FollowFragment newInstance() {
        FollowFragment fragment = new FollowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_follow, container, false);
        final Button followButton = root.findViewById(R.id.follow);
        final EditText useridEditText = root.findViewById(R.id.userid);


        followButton.setOnClickListener(event -> {
            String followeeUserid = useridEditText.getText().toString();
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            firestoreAPIApi.followUser(followeeUserid, userid,
                    new RerenderLeaderboardCallback(new LeaderboardFragment(), getParentFragmentManager()));
            replaceFragment(new LeaderboardFragment());
        });

        return root;
    }

    private void replaceFragment(Fragment f) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction t = fm.beginTransaction().replace(R.id.nav_host_fragment,f);
        t.addToBackStack(null);
        t.commit();
    }
}