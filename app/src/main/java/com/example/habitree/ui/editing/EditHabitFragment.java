package com.example.habitree.ui.editing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitree.R;
import com.example.habitree.api.HabitApi;
import com.example.habitree.model.HabitModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditHabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditHabitFragment extends Fragment {

    private final HabitModel h;
    private EditHabitFragment(HabitModel h) {this.h = h;}
    public static EditHabitFragment newInstance(HabitModel h) {
        EditHabitFragment fragment = new EditHabitFragment(h);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_habit, container, false);
        final TextView habitName = root.findViewById(R.id.habit_name);
        final EditText habitCurrent = root.findViewById(R.id.habit_current);
        final EditText habitGoal = root.findViewById(R.id.habit_goal);


        habitName.setText(String.format("%s", h.name));
        habitCurrent.setText(String.format("%s", h.current));
        habitGoal.setText(String.format("%s", h.goal));


        Button button_save = (Button) root.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    onSave(h,
                            habitName.getText().toString(),
                            Integer.parseInt(habitCurrent.getText().toString()),
                            Integer.parseInt(habitGoal.getText().toString()));
                    getParentFragmentManager().popBackStack();
                }
                catch (NumberFormatException e) {
                    Log.e("EDIT", "Onsave int parse fail" + e.getMessage());
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button button_complete = (Button) root.findViewById(R.id.button_complete);
        button_complete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    onComplete(h,
                            habitName.getText().toString(),
                            Integer.parseInt(habitGoal.getText().toString()));
                    getParentFragmentManager().popBackStack();
            }
                catch (NumberFormatException e) {
                Log.e("EDIT", "onComplete int parse fail" + e.getMessage());
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
            }
        });

        Button button_remove = (Button) root.findViewById(R.id.button_remove);
        button_remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onRemove(h);
            }
        });
        return root;
    }

    private void onSave(HabitModel h, String habitName, int habitCurrent, int habitGoal) {
        h.name = habitName;
        h.current = habitCurrent;
        h.goal = habitGoal;
        Log.d("EditSave", h.toString());
        HabitApi.updateHabit(this.getContext(), h);
    }
    private void onComplete(HabitModel h, String habitName, int habitGoal) {
        h.current = habitGoal;
        h.name = habitName;
        Log.d("EditComplete", h.toString());
        HabitApi.updateHabit(this.getContext(), h);
    }
    private void onRemove(HabitModel h) {
        Log.d("EditRemove", h.toString());
        Toast.makeText(getContext(), "Not implemented", Toast.LENGTH_SHORT).show();
    }
}