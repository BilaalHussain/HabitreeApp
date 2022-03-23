package com.example.habitree.ui.editing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
        final Spinner categorySpinner = root.findViewById(R.id.category_spinner);
        final Spinner frequencySpinner = root.findViewById(R.id.frequency_spinner);
        final EditText targetInput = root.findViewById(R.id.target_input);


        habitName.setText(String.format("%s", h.name));
        targetInput.setText(String.format("%s", h.goal));

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.frequencies_array,
                android.R.layout.simple_spinner_item
        );

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAdapter);
        frequencySpinner.setAdapter(frequencyAdapter);

        Button button_save = (Button) root.findViewById(R.id.save_habit_button);
        button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    onSave(h,
                            habitName.getText().toString(),
                            0,
                            Integer.parseInt(targetInput.getText().toString()));
                    getParentFragmentManager().popBackStack();
                }
                catch (NumberFormatException e) {
                    Log.e("EDIT", "Onsave int parse fail" + e.getMessage());
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        Button button_complete = (Button) root.findViewById(R.id.button_complete);
//        button_complete.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                try {
//                    onSave(h,
//                            habitName.getText().toString(),
//                            0,
//                            Integer.parseInt(targetInput.getText().toString()));
//                    getParentFragmentManager().popBackStack();
//            }
//                catch (NumberFormatException e) {
//                Log.e("EDIT", "onComplete int parse fail" + e.getMessage());
//                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
//            }
//            }
//        });

        Button button_remove = (Button) root.findViewById(R.id.delete_habit_button);
        button_remove.setOnClickListener(v -> onRemove(h));
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