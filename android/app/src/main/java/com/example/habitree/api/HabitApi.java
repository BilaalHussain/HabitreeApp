package com.example.habitree.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.habitree.database.HabitContract;
import com.example.habitree.database.HabitDbHelper;
import com.example.habitree.model.DailyHabit;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.WeeklyHabit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HabitApi {
    HabitDbHelper dbHelper;
    Gson gson = new Gson();

    public HabitApi(Context context) {
        synchronized (HabitApi.class) {
            dbHelper = new HabitDbHelper(context);
        }
    }

    private List<HabitModel> loadHabitsFromDisk() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                HabitContract.HabitEntry.COLUMN_NAME_ID,
                HabitContract.HabitEntry.COLUMN_NAME_NAME,
                HabitContract.HabitEntry.COLUMN_NAME_CATEGORY,
                HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED,
                HabitContract.HabitEntry.COLUMN_NAME_TYPE,
                HabitContract.HabitEntry.COLUMN_NAME_TAGS,
        };

        Cursor cursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<HabitModel> habits = new ArrayList<>();
        while(cursor.moveToNext()) {
            String habitId = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_ID));
            String sCategory = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_CATEGORY));
            String sName = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_NAME));
            String sDaysCompleted = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED));
            String sTag = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_TAGS));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_TYPE));

            HabitModel habit;
            // its a daily habit
            Type listType = new TypeToken<ArrayList<Date>>(){}.getType();
            List<Date> daysCompleted = gson.fromJson(sDaysCompleted, listType);
            if (type == 0) {
                habit = new DailyHabit(
                        UUID.fromString(habitId),
                        sName,
                        HabitModel.Category.ACADEMIC,
                        new ArrayList<>(), //TODO
                        new ArrayList<>() //TODO
                );
            } else {
                //its a weekly habit
                habit = new WeeklyHabit(
                        UUID.fromString(habitId),
                        sName,
                        HabitModel.Category.ACADEMIC,
                        new ArrayList<>(), //TODO
                        new ArrayList<>(), //TODO
                        type
                );
            }
            habits.add(habit);
        }
        cursor.close();
        return habits;
    }

    private void saveNewHabitToDisk(HabitModel habit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME_ID, habit.id.toString());
        values.put(HabitContract.HabitEntry.COLUMN_NAME_CATEGORY, habit.category.toString());
        values.put(HabitContract.HabitEntry.COLUMN_NAME_NAME, habit.name);
        values.put(HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED, gson.toJson(habit.daysHabitCompleted));
        if (habit instanceof WeeklyHabit) {
            values.put(HabitContract.HabitEntry.COLUMN_NAME_TYPE, ((WeeklyHabit) habit).target );
        } else {
            values.put(HabitContract.HabitEntry.COLUMN_NAME_TYPE, 0);
        }
        values.put(HabitContract.HabitEntry.COLUMN_NAME_TAGS, new ArrayList<>().toString());

        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }

    private void saveHabitToDisk(HabitModel habit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = HabitContract.HabitEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = { habit.id.toString() };

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME_CATEGORY, habit.category.toString());
        values.put(HabitContract.HabitEntry.COLUMN_NAME_NAME, habit.name);
        values.put(HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED, new ArrayList<>().toString());
        if (habit instanceof WeeklyHabit) {
            values.put(HabitContract.HabitEntry.COLUMN_NAME_TYPE, ((WeeklyHabit) habit).target );
        } else {
            values.put(HabitContract.HabitEntry.COLUMN_NAME_TYPE, 0);
        }
        values.put(HabitContract.HabitEntry.COLUMN_NAME_TAGS, new ArrayList<>().toString());

        long newRowId = db.update(
                HabitContract.HabitEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void createHabit(HabitModel habit) {
        synchronized (HabitApi.class) {
            saveNewHabitToDisk(habit);
        }
    }

    public List<HabitModel> getAllHabits() {
        synchronized (HabitApi.class) {
            return loadHabitsFromDisk();
        }
    }

    public void updateHabit(HabitModel habit) {
        synchronized (HabitApi.class) {
            saveHabitToDisk(habit);
        }
    }

    // Todo: Add remaining CRUD operations to api
}
