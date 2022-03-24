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

public class ViewTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<TagModel> currentTags;

    public void setCurrentTags(List<TagModel> newTags) {
        currentTags = newTags;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_tag, parent, false);
        return new TagViewHolder(view);
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
        final TextView tagName = itemView.findViewById(R.id.tag_name);
        float radius = itemView.getResources().getDimension(R.dimen.small);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,radius)
                .build();
        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        int[][] states = new int[][] {new int[] { android.R.attr.state_enabled}};

        public void setTag(TagModel tag) {
            tagName.setText(tag.name);
            int[] stateColors = new int[] { tag.color };
            ColorStateList myList = new ColorStateList(states, stateColors);
            shapeDrawable.setFillColor(myList);
            tagName.setBackground(shapeDrawable);
        }

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
