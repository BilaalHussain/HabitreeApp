package com.example.habitree.ui.leaderboard;

public class LeaderboardFriendModel implements Comparable<LeaderboardFriendModel> {
    String friendName;
    Float friendScore;

    public LeaderboardFriendModel(String friendName, Float friendScore) {
        this.friendName = friendName;
        this.friendScore = friendScore;
    }

    public String getFriendName() {
        return friendName;
    }

    public Float getFriendScore() {
        return friendScore;
    }

    @Override
    public int compareTo(LeaderboardFriendModel leaderboardFriendModel) {
        Float score = getFriendScore();
        if (score == null || leaderboardFriendModel.getFriendScore() == null) {
            return 0;
        }
        return score.compareTo(leaderboardFriendModel.getFriendScore());
    }
}
