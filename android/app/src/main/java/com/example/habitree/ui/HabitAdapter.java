package com.example.habitree.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.R;
import com.example.habitree.listener.CheckBoxTapped;
import com.example.habitree.listener.EventListener;
import com.example.habitree.listener.HabitTapped;
import com.example.habitree.model.HabitModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previous;

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
        final CheckBox habitTitleAndCheck = itemView.findViewById(R.id.habit_name);
        final TextView progressNumber = itemView.findViewById(R.id.progress_number);
        final RecyclerView tagList = itemView.findViewById(R.id.tag_list);
        final ViewTagAdapter viewTagAdapter = new ViewTagAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        EventListener eventListener;

        public void setHabit(HabitModel habit) {
            final LocalDate today = LocalDate.now();
            final LocalDate thisPastSunday = today.with(previous(SUNDAY));
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date lastSundayDate = Date.from(thisPastSunday.atStartOfDay(defaultZoneId).toInstant());

            habitTitleAndCheck.setText(habit.name);
            habitTitleAndCheck.setChecked(!habit.isToDoToday());
            progressNumber.setText(habit.getReadableStatusString(lastSundayDate));
            itemView.setOnClickListener(v -> eventListener.onEvent(new HabitTapped(habit.id)));
            habitTitleAndCheck.setOnClickListener(view -> eventListener.onEvent(new CheckBoxTapped(habit.id)));

            if (!habit.tags.isEmpty()) {
                tagList.setVisibility(View.VISIBLE);
                tagList.setAdapter(viewTagAdapter);
                tagList.setLayoutManager(linearLayoutManager);
                viewTagAdapter.setCurrentTags(habit.tags);
            } else {
                tagList.setVisibility(View.GONE);
            }
        }

        public HabitViewHolder(@NonNull View itemView, EventListener eventListener) {
            super(itemView);
            this.eventListener = eventListener;
        }
    }
}
