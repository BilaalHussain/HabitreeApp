package com.example.habitree.ui.leaderboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.R;

import java.util.ArrayList;
import java.util.Random;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {
    Context context;
    ArrayList<LeaderboardFriendModel> leaderboardFriends;

    public LeaderboardAdapter(Context context, ArrayList<LeaderboardFriendModel> leaderboardFriends) {
        this.context = context;
        this.leaderboardFriends = leaderboardFriends;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_leaderboard_row, parent, false);
        return new LeaderboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(leaderboardFriends.get(position).getFriendName());
        holder.tvScore.setText("Score: " + Integer.toString(Math.round(leaderboardFriends.get(position).getFriendScore())));
        Integer[] avatars = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4};
        Integer randomAvatar = new Random().nextInt(4);
        holder.ivAvatar.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), avatars[randomAvatar], null));

    }

    @Override
    public int getItemCount() {
        return leaderboardFriends.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView tvName, tvScore;

        ImageView ivAvatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.friendName);
            tvScore = itemView.findViewById(R.id.friendScore);
            ivAvatar = itemView.findViewById(R.id.avatar);
        }
    }
}
