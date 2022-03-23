package com.example.habitree.model;

public class BinaryTarget extends Target {
    boolean isComplete;


    @Override
    public double getCompletionProgress() {
        return isComplete ? 1 : 0;
    }

    @Override
    public void complete() {
        isComplete = true;
    }
}
