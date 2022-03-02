package com.example.habitree.api;

import android.content.Context;
import android.util.Log;

import com.example.habitree.model.HabitModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HabitApi {

    private static List<HabitModel> loadHabitsFromDisk(Context context) {
        // Todo: Pull all real habit ids from a file instead of mocking here
        UUID testId = UUID.fromString("2f8bd149-1925-4b75-a675-f50b6a268d23");
        List<UUID> habitIds = Arrays.asList(testId);
        List<HabitModel> habits = new ArrayList<>();

        for (UUID habitId : habitIds) {
            try {
                FileInputStream fis = context.openFileInput(String.valueOf(habitId));
                ObjectInputStream is = new ObjectInputStream(fis);
                HabitModel habit = (HabitModel) is.readObject();
                is.close();
                fis.close();
                habits.add(habit);
            } catch (IOException e) {
                // temporarily mock the set of saved habits if none exist
                e.printStackTrace();
                habits.add(new HabitModel(testId, "workout", 0, 10));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return habits;
    }

    private static void saveHabitToDisk(Context context, HabitModel habit) {
        try {
            FileOutputStream fos = context.openFileOutput(String.valueOf(habit.id), Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(habit);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<HabitModel> getAllHabits(Context context) {
        synchronized (HabitApi.class) {
            return loadHabitsFromDisk(context);
        }
    }

    public static void updateHabit(Context context, HabitModel habit) {
        synchronized (HabitApi.class) {
            saveHabitToDisk(context, habit);
        }
    }

    // Todo: Add remaining CRUD operations to api
}
