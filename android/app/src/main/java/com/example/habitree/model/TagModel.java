package com.example.habitree.model;

import android.graphics.Color;
import java.io.Serializable;

public class TagModel implements Serializable {
    String name;
    Color color; // Color as hex
    // we can also add a color associated with a tag


    TagModel(String name) {
        this.name = name;
        this.color = new Color(); // TODO: Take color param
    }
}
