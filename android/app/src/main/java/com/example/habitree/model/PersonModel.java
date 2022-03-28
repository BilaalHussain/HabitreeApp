package com.example.habitree.model;

import java.util.List;

public class PersonModel {
    ScoreModel scores;
    String name;
    String uuid;

    public PersonModel(List<Float> scores, String name, String uuid) {
        this.scores = new ScoreModel(scores, uuid);
        this.name = name;
        this.uuid = uuid;
    }

    public ScoreModel getScore() {
        return this.scores;
    }

    public String getName() {
        if (this.name != null) {
            return this.name;
        } else {
            return this.uuid.substring(0, 10) + "...";
        }
    }
}
