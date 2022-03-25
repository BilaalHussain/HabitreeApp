package com.example.habitree.model;

import java.io.Serializable;
import java.net.URI;

public class TreeModel implements Serializable {
    public String title;
    public ScoreModel score;

    public TreeModel(String title, ScoreModel score) {
        this.title = title;
        this.score = score;
    }
}
