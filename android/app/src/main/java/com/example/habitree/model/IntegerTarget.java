package com.example.habitree.model;

import android.util.Log;

public class IntegerTarget extends Target {
    public int targetValue;
    public int currentValue;

    public IntegerTarget(int t, int c) {
        targetValue = t;
        currentValue = c;
    }

    @Override
    public double getCompletionProgress() {
        return ((double) currentValue)/((double) targetValue);
    }

    @Override
    public void complete() {
        if (currentValue >= targetValue ){
            Log.w("HABIT_TARGET", "Attempted to complete already complete habit");
            return;
        }
        currentValue +=1 ;
    }
}
