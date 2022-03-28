package com.example.habitree.ui.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.R;
import com.example.habitree.listener.EventListener;
import com.example.habitree.listener.PersonTapped;

import java.util.ArrayList;
import java.util.Random;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.PersonViewHolder> {
    Context context;
    ArrayList<LeaderboardFriendModel> leaderboardFriends;

    private EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public LeaderboardAdapter(Context context, ArrayList<LeaderboardFriendModel> leaderboardFriends) {
        this.context = context;
        this.leaderboardFriends = leaderboardFriends;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_leaderboard_row, parent, false);
        return new PersonViewHolder(view, eventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.setPerson(context, leaderboardFriends.get(position));
    }

    @Override
    public int getItemCount() {
        return leaderboardFriends.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName = itemView.findViewById(R.id.friendName);
        final TextView tvScore = itemView.findViewById(R.id.friendScore);
        final ImageView ivAvatar = itemView.findViewById(R.id.avatar);
        EventListener eventListener;

        public void setPerson(Context context, LeaderboardFriendModel person) {
            tvName.setText(person.getFriendName());
            tvScore.setText("Score: " + Integer.toString(Math.round(person.getFriendScore())));
            Integer[] avatars = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4};
            Integer randomAvatar = new Random().nextInt(4);
            ivAvatar.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), avatars[randomAvatar], null));
            itemView.setOnClickListener(v -> eventListener.onEvent(new PersonTapped(person)));
        }

        public PersonViewHolder(@NonNull View itemView, EventListener eventListener) {
            super(itemView);
            this.eventListener = eventListener;
        }
    }
}
