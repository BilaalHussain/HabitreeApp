package com.example.habitree.ui.leaderboard;

import com.example.habitree.model.PersonModel;

public class AddFolloweeCallback {
    LeaderboardAdapter adapter;

    public AddFolloweeCallback(LeaderboardAdapter adapter) {
        this.adapter = adapter;
    }

    public void execute(PersonModel followee) {
        this.adapter.addFolloweeToLeaderboard(followee);
    }
}
