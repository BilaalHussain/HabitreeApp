package com.example.habitree.model;

import java.util.List;

public class PersonModel {
    List<Float> scores;
    String name;

    public PersonModel(List<Float> scores, String name) {
        this.scores = scores;
        this.name = name;
    }

    public List<Float>getScores() {
        return this.scores;
    }

    public String getName() {
        return this.name;
    }
}
