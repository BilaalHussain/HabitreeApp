package com.example.habitree.model;

import java.util.List;

public class PersonModel {
    ScoreModel scores;
    String name;

    public PersonModel(List<Float> scores, String name, String uuid) {
        this.scores = new ScoreModel(scores, uuid);
        this.name = name;
    }

    public ScoreModel getScore() {
        return this.scores;
    }

    public String getName() {
        return this.name;
    }
}
