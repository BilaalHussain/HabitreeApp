package com.example.habitree.model;

import android.graphics.Color;
import java.io.Serializable;
import java.util.UUID;

public class TagModel implements Serializable {
    public UUID id;
    public String name;
    public Color color; // Color as hex
    public boolean isEditing; // literally just used in the adapter
    // we can also add a color associated with a tag


    public TagModel(UUID id, String name) {
        this.name = name;
        this.id = id;
        this.color = new Color(); // TODO: Take color param
        this.isEditing = true;
    }
}
