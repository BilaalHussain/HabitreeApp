package com.example.habitree.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HabitDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";


    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + HabitContract.HabitEntry.TABLE_NAME + " (" +
                    HabitContract.HabitEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    HabitContract.HabitEntry.COLUMN_NAME_NAME + " TEXT," +
                    HabitContract.HabitEntry.COLUMN_NAME_CATEGORY + " TEXT," +
                    HabitContract.HabitEntry.COLUMN_NAME_DAYS_COMPLETED + " TEXT," +
                    HabitContract.HabitEntry.COLUMN_NAME_TYPE + " INT," +
                    HabitContract.HabitEntry.COLUMN_NAME_GEOFENCE_INFO + " TEXT," +
                    HabitContract.HabitEntry.COLUMN_NAME_TAGS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HabitContract.HabitEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
