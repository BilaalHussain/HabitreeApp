package com.example.habitree.ui;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitree.R;
import com.example.habitree.listener.DeleteTagTapped;
import com.example.habitree.listener.EventListener;
import com.example.habitree.listener.ToggleIsEditing;
import com.example.habitree.listener.UpdateTag;
import com.example.habitree.model.TagModel;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.Arrays;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    List<TagModel> currentTags;

    public void setCurrentTags(List<TagModel> newTags) {
        currentTags = newTags;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.create_tag_layout, parent, false);
        return new TagViewHolder(view, eventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagViewHolder) holder).setTag(currentTags.get(position));
    }

    @Override
    public int getItemCount() {
        return currentTags.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        final EditText tagNameEditText = itemView.findViewById(R.id.edit_tag_name);
        final TextView tagNameText = itemView.findViewById(R.id.tag_name);
        final ImageButton deleteTagButton = itemView.findViewById(R.id.delete_tag_button);
        final ImageButton editTagButton = itemView.findViewById(R.id.edit_tag_button);
        final ImageButton doneEditingButton = itemView.findViewById(R.id.done_editing_button);
        final Spinner colorSpinner = itemView.findViewById(R.id.color_picker);
        float radius = itemView.getResources().getDimension(R.dimen.small);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,radius)
                .build();
        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        int[][] states = new int[][] {new int[] { android.R.attr.state_enabled}};

        EventListener eventListener;

        public void setTag(TagModel tag) {
            tagNameText.setText(tag.name);
            int[] stateColors = new int[] { tag.color };
            ColorStateList myList = new ColorStateList(states, stateColors);
            shapeDrawable.setFillColor(myList);
            tagNameText.setBackground(shapeDrawable);
            tagNameEditText.setText(tag.name);
            doneEditingButton.setOnClickListener(view -> {
                if (tagNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Please give all tags a name", Toast.LENGTH_SHORT).show();
                } else {
                    eventListener.onEvent(new UpdateTag(
                            tag.id,
                            tagNameEditText.getText().toString(),
                            (Integer) colorSpinner.getSelectedItem()
                    ));
                }
            });

            if (tag.isEditing) {
                deleteTagButton.setVisibility(View.VISIBLE);
                doneEditingButton.setVisibility(View.VISIBLE);
                editTagButton.setVisibility(View.GONE);
                tagNameEditText.setVisibility(View.VISIBLE);
                tagNameText.setVisibility(View.GONE);
                colorSpinner.setVisibility(View.VISIBLE);
            } else {
                deleteTagButton.setVisibility(View.GONE);
                doneEditingButton.setVisibility(View.GONE);
                editTagButton.setVisibility(View.VISIBLE);
                tagNameEditText.setVisibility(View.GONE);
                tagNameText.setVisibility(View.VISIBLE);
                colorSpinner.setVisibility(View.GONE);
            }

            editTagButton.setOnClickListener(view -> {
                eventListener.onEvent(new ToggleIsEditing(tag.id, true));
            });

            deleteTagButton.setOnClickListener(view -> {
                eventListener.onEvent(new DeleteTagTapped(tag.id));
            });

            // set the spinner
            Integer[] colors = TagModel.getValidTagColors(itemView.getContext());
            SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(
                    itemView.getContext(),
                    colors
            );
            int index = Arrays.asList(colors).indexOf(tag.color);
            colorSpinner.setAdapter(adapter);
            if (index != -1) { colorSpinner.setSelection(index); }
        }

        public TagViewHolder(@NonNull View itemView, EventListener eventListener) {
            super(itemView);
            this.eventListener = eventListener;
        }
    }
}
