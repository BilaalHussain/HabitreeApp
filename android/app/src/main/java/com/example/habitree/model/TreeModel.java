package com.example.habitree.model;

import java.io.Serializable;
import java.net.URI;

public class TreeModel implements Serializable {
    public String title;
    public ScoreModel score;
    public boolean sharable;

    public TreeModel(String title, ScoreModel score, boolean sharable) {
        this.title = title;
        this.score = score;
        this.sharable = sharable;
    }
}
