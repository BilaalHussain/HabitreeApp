package com.example.habitree.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.habitree.database.HabitContract;
import com.example.habitree.database.HabitDbHelper;
import com.example.habitree.geofence.GeofenceInfo;
import com.example.habitree.model.DailyHabit;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.TagModel;
import com.example.habitree.model.WeeklyHabit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HabitApi {
    private static final String TAG = "HabitAPI";
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
                HabitContract.HabitEntry.COLUMN_NAME_GEOFENCE_INFO,
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
        while (cursor.moveToNext()) {
            String habitId = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_ID));
            String sCategory = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_CATEGORY));
            String sName = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_NAME));
            String sDaysCompleted = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED));
            String sTag = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_TAGS));
            String sGeofence = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_GEOFENCE_INFO));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_TYPE));

            HabitModel habit;
            Type dateListType = new TypeToken<ArrayList<Date>>() {
            }.getType();
            List<Date> daysCompleted = gson.fromJson(sDaysCompleted, dateListType);

            Type tagListType = new TypeToken<ArrayList<TagModel>>() {
            }.getType();
            List<TagModel> tags = gson.fromJson(sTag, tagListType);

            GeofenceInfo geofenceInfo = gson.fromJson(sGeofence, GeofenceInfo.class);

            HabitModel.Category category = HabitModel.stringToCategory(sCategory);
            // its a daily habit
            if (type == 0) {
                habit = new DailyHabit(
                        UUID.fromString(habitId),
                        sName,
                        category,
                        daysCompleted,
                        tags,
                        geofenceInfo
                );
            } else {
                //its a weekly habit
                habit = new WeeklyHabit(
                        UUID.fromString(habitId),
                        sName,
                        category,
                        daysCompleted,
                        tags,
                        type,
                        geofenceInfo
                );
            }
            habits.add(habit);
        }
        cursor.close();
        return habits;
    }

    private void saveNewHabitToDisk(
            UUID habitId,
            String habitName,
            HabitModel.Category category,
            List<TagModel> tags,
            int targetAmount,
            GeofenceInfo geofenceInfo
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME_ID, habitId.toString());
        values.put(HabitContract.HabitEntry.COLUMN_NAME_CATEGORY, category.toString());
        values.put(HabitContract.HabitEntry.COLUMN_NAME_NAME, habitName);
        values.put(HabitContract.HabitEntry.COLUMN_NAME_TYPE, targetAmount);
        values.put(HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED, gson.toJson(new ArrayList<>()));
        values.put(HabitContract.HabitEntry.COLUMN_NAME_TAGS, gson.toJson(tags));
        values.put(HabitContract.HabitEntry.COLUMN_NAME_GEOFENCE_INFO, gson.toJson(geofenceInfo));

        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }

    private void saveHabitToDisk(
            UUID habitId,
            String habitName,
            HabitModel.Category category,
            List<TagModel> tags,
            int targetAmount,
            GeofenceInfo geoFenceInfo
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = HabitContract.HabitEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = {habitId.toString()};

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME_ID, habitId.toString());
        values.put(HabitContract.HabitEntry.COLUMN_NAME_CATEGORY, category.toString());
        values.put(HabitContract.HabitEntry.COLUMN_NAME_NAME, habitName);
        values.put(HabitContract.HabitEntry.COLUMN_NAME_TYPE, targetAmount);
        values.put(HabitContract.HabitEntry.COLUMN_NAME_TAGS, gson.toJson(tags));
        values.put(HabitContract.HabitEntry.COLUMN_NAME_GEOFENCE_INFO, gson.toJson(geoFenceInfo));

        long newRowId = db.update(
                HabitContract.HabitEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    private void deleteHabitFromDisk(UUID habitId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = HabitContract.HabitEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = {habitId.toString()};

        int deletedRows = db.delete(HabitContract.HabitEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void updateHabitDatesCompletedOnDisk(UUID habitId, List<Date> date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = HabitContract.HabitEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = {habitId.toString()};

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED, gson.toJson(date));
        values.put(HabitContract.HabitEntry.COLUMN_NAME_ID, habitId.toString());

        long newRowId = db.update(
                HabitContract.HabitEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public HabitModel getHabitById(UUID findId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                HabitContract.HabitEntry.COLUMN_NAME_ID,
                HabitContract.HabitEntry.COLUMN_NAME_NAME,
                HabitContract.HabitEntry.COLUMN_NAME_CATEGORY,
                HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED,
                HabitContract.HabitEntry.COLUMN_NAME_TYPE,
                HabitContract.HabitEntry.COLUMN_NAME_TAGS,
                HabitContract.HabitEntry.COLUMN_NAME_GEOFENCE_INFO
        };

        String selection = HabitContract.HabitEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = {findId.toString()};

        Cursor cursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        if (cursor.getCount() != 1) {
            Log.e(TAG, "Did not find habit with ID" + findId.toString());
            return null;
        }
        cursor.moveToNext();
        String habitId = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_ID));
        String sCategory = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_CATEGORY));
        String sName = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_NAME));
        String sDaysCompleted = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED));
        String sTag = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_TAGS));
        String sGeofence = cursor.getString(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_GEOFENCE_INFO));

        int type = cursor.getInt(cursor.getColumnIndexOrThrow(HabitContract.HabitEntry.COLUMN_NAME_TYPE));

        HabitModel habit;
        Type dateListType = new TypeToken<ArrayList<Date>>() {
        }.getType();
        List<Date> daysCompleted = gson.fromJson(sDaysCompleted, dateListType);

        Type tagListType = new TypeToken<ArrayList<TagModel>>() {}.getType();
        List<TagModel> tags = gson.fromJson(sTag, tagListType);

        GeofenceInfo geofenceInfo = gson.fromJson(sGeofence, GeofenceInfo.class);
        HabitModel.Category category = HabitModel.stringToCategory(sCategory);
        // its a daily habit
        if (type == 0) {
            habit = new DailyHabit(
                    UUID.fromString(habitId),
                    sName,
                    category,
                    daysCompleted,
                    tags,
                    geofenceInfo
            );
        } else {
            //its a weekly habit
            habit = new WeeklyHabit(
                    UUID.fromString(habitId),
                    sName,
                    category,
                    daysCompleted,
                    tags,
                    type,
                    geofenceInfo
            );
        }

        cursor.close();
        return habit;
    }

    public void createHabit(
            UUID habitId,
            String habitName,
            HabitModel.Category category,
            List<TagModel> tags,
            int targetAmount,
            GeofenceInfo geofenceInfo
    ) {
        synchronized (HabitApi.class) {
            saveNewHabitToDisk(habitId, habitName, category, tags, targetAmount,geofenceInfo);
        }
    }

    public List<HabitModel> getAllHabits() {
        synchronized (HabitApi.class) {
            return loadHabitsFromDisk();
        }
    }

    public void updateHabit(
            UUID habitId,
            String habitName,
            HabitModel.Category category,
            List<TagModel> tags,
            int targetAmount,
            GeofenceInfo geofenceInfo
    ) {
        synchronized (HabitApi.class) {
            saveHabitToDisk(habitId, habitName, category, tags, targetAmount, geofenceInfo);
        }
    }

    public void deleteHabit(UUID habitId) {
        synchronized (HabitApi.class) {
            deleteHabitFromDisk(habitId);
        }
    }

    public void updateHabitDatesCompleted(UUID habitId, List<Date> dates) {
        synchronized (HabitApi.class) {
            updateHabitDatesCompletedOnDisk(habitId, dates);
        }
    }

    // Todo: Add remaining CRUD operations to api
}
