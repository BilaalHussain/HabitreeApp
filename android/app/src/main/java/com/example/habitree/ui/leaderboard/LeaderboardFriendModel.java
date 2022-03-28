package com.example.habitree.ui.leaderboard;

import com.example.habitree.model.PersonModel;
import com.example.habitree.model.ScoreModel;

public class LeaderboardFriendModel implements Comparable<LeaderboardFriendModel> {
    String friendName;
    ScoreModel friendScore;

    public LeaderboardFriendModel(String friendName, ScoreModel friendScore) {
        this.friendName = friendName;
        this.friendScore = friendScore;
    }

    public LeaderboardFriendModel(PersonModel person) {
        this.friendName = person.getName();
        this.friendScore = person.getScore();
    }

    public String getFriendName() {
        return friendName;
    }

    public Float getFriendScore() {
        return friendScore.cleanSummaryScoreForLeaderboard();
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
