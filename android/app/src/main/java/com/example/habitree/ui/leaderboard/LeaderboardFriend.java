package com.example.habitree.ui.leaderboard;

public class LeaderboardFriend  {
    String friendName;
    String friendScore;

    public LeaderboardFriend(String friendName, String friendScore) {
        this.friendName = friendName;
        this.friendScore = friendScore;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendScore() {
        return friendScore;
    }
}
