package com.example.habitree.listener;

import com.example.habitree.ui.leaderboard.LeaderboardFriendModel;

public class PersonTapped implements Event {
    public LeaderboardFriendModel person;
    public PersonTapped(LeaderboardFriendModel person) {
        this.person = person;
    }
}
