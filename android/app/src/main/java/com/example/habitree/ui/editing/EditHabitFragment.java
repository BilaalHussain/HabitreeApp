package com.example.habitree.ui.editing;

import android.annotation.SuppressLint;
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
import com.example.habitree.model.DailyHabit;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.WeeklyHabit;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_habit, container, false);
        final TextView habitName = root.findViewById(R.id.habit_name);
        final Spinner categorySpinner = root.findViewById(R.id.category_spinner);
        final Spinner frequencySpinner = root.findViewById(R.id.frequency_spinner);
        final Spinner targetTypeSpinner = root.findViewById(R.id.target_type_spinner);
        final EditText repeatsInput = root.findViewById(R.id.repeats_input);
        final TextView repeatsText = root.findViewById(R.id.repeats_label);

        habitName.setText(String.format("%s", h.name));
        if (h instanceof WeeklyHabit) {
            repeatsInput.setVisibility(View.VISIBLE);
            repeatsText.setVisibility(View.VISIBLE);
            repeatsInput.setText(((WeeklyHabit) h).target);
        }

        // set the category spinner to contain a list of alll the current categories
        List<CharSequence> categories = Stream.of(HabitModel.Category.values())
                .map(HabitModel.Category::toString)
                .collect(Collectors.toList());
        ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categories
        );

        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.frequencies_array,
                android.R.layout.simple_spinner_item
        );

        ArrayAdapter<CharSequence> targetTypesAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.target_types,
                android.R.layout.simple_spinner_item
        );

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAdapter);
        targetTypeSpinner.setAdapter(targetTypesAdapter);
        frequencySpinner.setAdapter(frequencyAdapter);
        String[] targetTypes = getResources().getStringArray(R.array.target_types);

        targetTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // it is binary
                if (adapterView.getItemAtPosition(i).toString().equals(targetTypes[0])) {
                    repeatsInput.setVisibility(View.GONE);
                    repeatsText.setVisibility(View.GONE);
                } else {
                    // it is numeric
                    repeatsInput.setVisibility(View.VISIBLE);
                    repeatsText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });


        Button button_save = (Button) root.findViewById(R.id.save_habit_button);
        button_save.setOnClickListener(v -> {
            try {
                // it is binary
//                if (targetTypeSpinner.getSelectedItem().toString().equals(targetTypes[0])){
//                     = new BinaryTarget();
//                } else {
//                    newTarget = new IntegerTarget(
//                            Integer.parseInt(repeatsInput.getText().toString()), // target
//                            0   // current
//                    );
//                }
                HabitModel.Category selectedCategory = HabitModel.Category.valueOf(
                        categorySpinner.getSelectedItem().toString()
                );
                onSave(
                        h,
                        habitName.getText().toString(),
                        selectedCategory
                );
                getParentFragmentManager().popBackStack();
            }
            catch (NumberFormatException e) {
                Log.e("EDIT", "Onsave int parse fail" + e.getMessage());
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
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

    private void onSave(
            HabitModel h,
            String habitName,
            HabitModel.Category category
    ) {
        h.name = habitName;
        h.category = category;
        Log.d("EditSave", h.toString());
        HabitApi.updateHabit(this.getContext(), h);
    }
    private void onComplete(HabitModel h) {
        h.complete();
    }
    private void onRemove(HabitModel h) {
        Log.d("EditRemove", h.toString());
        Toast.makeText(getContext(), "Not implemented", Toast.LENGTH_SHORT).show();
    }
}