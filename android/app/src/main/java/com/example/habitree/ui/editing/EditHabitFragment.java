package com.example.habitree.ui.editing;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.habitree.listener.DeleteTagTapped;
import com.example.habitree.listener.ToggleIsEditing;
import com.example.habitree.listener.UpdateTag;
import com.example.habitree.model.DailyHabit;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.TagModel;
import com.example.habitree.model.WeeklyHabit;
import com.example.habitree.ui.HabitAdapter;
import com.example.habitree.ui.TagAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditHabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditHabitFragment extends Fragment {

    private final HabitModel h;
    private final Boolean isCreating;
    private HabitApi habitApi;
    private EditHabitFragment(HabitModel h, Boolean isCreating) {
        this.h = h;
        this.isCreating = isCreating;
    }
    public static EditHabitFragment newInstance(HabitModel h, Boolean isCreating) {
        EditHabitFragment fragment = new EditHabitFragment(h, isCreating);
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
        habitApi = new HabitApi(requireContext());
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_habit, container, false);
        final TextView habitName = root.findViewById(R.id.habit_name);
        final Spinner categorySpinner = root.findViewById(R.id.category_spinner);
        final Spinner habitTypeSpinner = root.findViewById(R.id.habit_type_spinner);
        final EditText repeatsInput = root.findViewById(R.id.repeats_input);
        final TextView repeatsText = root.findViewById(R.id.repeats_label);
        final RecyclerView tagList = root.findViewById(R.id.tags_list);
        final Button addTagButton = root.findViewById(R.id.add_tag_button);

        habitName.setText(String.format("%s", h.name));
        if (h instanceof WeeklyHabit) {
            repeatsInput.setVisibility(View.VISIBLE);
            repeatsText.setVisibility(View.VISIBLE);
            repeatsInput.setText(String.valueOf(((WeeklyHabit) h).target));
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

        ArrayAdapter<CharSequence> habitTypesAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.habit_types,
                android.R.layout.simple_spinner_item
        );

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAdapter);
        habitTypeSpinner.setAdapter(habitTypesAdapter);
        String[] habitTypes = getResources().getStringArray(R.array.habit_types);

        categorySpinner.setSelection(h.category.getIndex());
        habitTypeSpinner.setSelection(h instanceof DailyHabit ? 0 : 1);

        habitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // it is binary
                if (adapterView.getItemAtPosition(i).toString().equals(habitTypes[0])) {
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

        //set up tag recycler
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        TagAdapter tagAdapter = new TagAdapter();
        tagAdapter.setEventListener(event -> {
            if (event instanceof DeleteTagTapped) {
                // delete the tag that has the same id as the tag that is set to be deleted
                h.tags = h.tags.stream()
                        .filter(tag -> tag.id != ((DeleteTagTapped) event).id)
                        .collect(Collectors.toList());
                tagAdapter.setCurrentTags(h.tags);
            } else if (event instanceof UpdateTag) {
                h.tags = h.tags.stream()
                        .peek(tag -> {
                                if (tag.id == ((UpdateTag) event).tagId) {
                                    tag.name = ((UpdateTag) event).tagName;
                                    tag.isEditing = false;
                                }
                        })
                        .collect(Collectors.toList());
                tagAdapter.setCurrentTags(h.tags);
            } else if (event instanceof ToggleIsEditing) {
                h.tags = h.tags.stream()
                        .peek(tag -> {
                            if (tag.id == ((ToggleIsEditing) event).tagId) {
                                tag.isEditing = ((ToggleIsEditing) event).newEditState;
                            }
                        })
                        .collect(Collectors.toList());
                tagAdapter.setCurrentTags(h.tags);
            }
        }); // need to listen for a tag to be deleted

        tagList.setAdapter(tagAdapter);
        tagList.setLayoutManager(linearLayoutManager);

        tagAdapter.setCurrentTags(h.tags);

        addTagButton.setOnClickListener(v -> {
            @SuppressLint("DefaultLocale") TagModel newTag = new TagModel(UUID.randomUUID(), String.format("tag%d", (h.tags.size() + 1)));
            h.tags.add(newTag);
            tagAdapter.setCurrentTags(h.tags);
        });


        Button button_save = (Button) root.findViewById(R.id.save_habit_button);
        button_save.setOnClickListener(v -> {
            try {
                HabitModel.Category selectedCategory = HabitModel.stringToCategory(
                        categorySpinner.getSelectedItem().toString()
                );
                int targetAmount;
                if (habitTypeSpinner.getSelectedItem().equals("Daily")) {
                    targetAmount = 0;
                } else {
                    targetAmount = Integer.parseInt(repeatsInput.getText().toString());
                }

                if (habitName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please give your habit a name", Toast.LENGTH_SHORT).show();
                } else {
                    // set all the tags to not be editing
                    List<TagModel> newTags = h.tags.stream()
                            .peek(tag -> tag.isEditing = false)
                            .collect(Collectors.toList());
                    onSave(
                            h.id,
                            habitName.getText().toString(),
                            selectedCategory,
                            newTags,
                            targetAmount
                    );
                    getParentFragmentManager().popBackStack();
                }
            }
            catch (NumberFormatException e) {
                Log.e("EDIT", "Onsave int parse fail" + e.getMessage());
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
            }

        });

        Button button_remove = (Button) root.findViewById(R.id.delete_habit_button);
        button_remove.setOnClickListener(v -> {
            onRemove(h);
            getParentFragmentManager().popBackStack();
        });
        return root;
    }

    private void onSave(
            UUID habitId,
            String habitName,
            HabitModel.Category category,
            List<TagModel> tags,
            int targetAmount
    ) {
        if (isCreating) {
            habitApi.createHabit(habitId, habitName, category, tags, targetAmount);
        } else {
            habitApi.updateHabit(habitId, habitName, category, tags, targetAmount);
        }
    }
    private void onComplete(HabitModel h) {
        h.complete();
    }
    private void onRemove(HabitModel h) {
        Log.d("EditRemove", h.toString());
        habitApi.deleteHabit(h.id);
    }
}