package com.example.habitree.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.R;
import com.example.habitree.listener.EventListener;
import com.example.habitree.listener.HabitTapped;
import com.example.habitree.model.HabitModel;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    List<HabitModel> currentHabits;

    public void setCurrentHabits(List<HabitModel> newHabits) {
        currentHabits = newHabits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_layout, parent, false);
        return new HabitViewHolder(view, eventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HabitViewHolder) holder).setHabit(currentHabits.get(position));
    }

    @Override
    public int getItemCount() {
        return currentHabits.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        final TextView habitTitle = itemView.findViewById(R.id.habit_name);
        final TextView progressNumber = itemView.findViewById(R.id.progress_number);
        EventListener eventListener;

        public void setHabit(HabitModel habit) {
            habitTitle.setText(habit.name);
            String progress = "10/10";
            progressNumber.setText(progress);
            itemView.setOnClickListener(v -> eventListener.onEvent(new HabitTapped(habit.id)));
        }

        public HabitViewHolder(@NonNull View itemView, EventListener eventListener) {
            super(itemView);
            this.eventListener = eventListener;
        }
    }
}
