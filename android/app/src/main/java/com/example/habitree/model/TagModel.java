package com.example.habitree.model;

import android.content.Context;
import android.graphics.Color;

import com.example.habitree.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class TagModel implements Serializable {
    public UUID id;
    public String name;
    public Integer color; // Color as string
    public boolean isEditing; // literally just used in the adapter
    // we can also add a color associated with a tag


    public TagModel(UUID id, String name, Integer color) {
        this.name = name;
        this.id = id;
        this.color = color; // TODO: Take color param
        this.isEditing = true;
    }

    public static Integer[] getValidTagColors(Context context) {
        return new Integer[]{
                Color.valueOf(context.getColor(R.color.red)).toArgb(),
                Color.valueOf(context.getColor(R.color.yellow)).toArgb(),
                Color.valueOf(context.getColor(R.color.green)).toArgb(),
                Color.valueOf(context.getColor(R.color.pink)).toArgb(),
                Color.valueOf(context.getColor(R.color.teal_200)).toArgb(),
                Color.valueOf(context.getColor(R.color.purple_500)).toArgb()
        };
    }
}
