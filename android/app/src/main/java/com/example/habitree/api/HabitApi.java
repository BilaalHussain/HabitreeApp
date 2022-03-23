package com.example.habitree.api;

import android.content.Context;

import com.example.habitree.model.DailyHabit;
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
        UUID testId1 = UUID.fromString("2f8bd149-1925-4b75-a675-f50b6a268d23");
        UUID testId2 = UUID.fromString("2f8bd149-1925-4b75-a675-f50b6a268d24");
        UUID testId3 = UUID.fromString("2f8bd149-1925-4b75-a675-f50b6a268d25");
        UUID testId4 = UUID.fromString("2f8bd149-1925-4b75-a675-f50b6a268d26");
        UUID testId5 = UUID.fromString("2f8bd149-1925-4b75-a675-f50b6a268d28");
        List<UUID> habitIds = Arrays.asList(testId1);
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
                habits.add(new DailyHabit(
                        testId1,
                        "Drinking Water",
                        HabitModel.Category.ACADEMIC,
                        new ArrayList<>()
                ));
//                habits.add(new HabitModel(testId2, "Exercising", 1, 4));
//                habits.add(new HabitModel(testId4, "Crying", 15, 100));
//                habits.add(new HabitModel(testId5, "Laying on the bed", 12, 30));
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
