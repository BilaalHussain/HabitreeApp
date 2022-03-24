package com.example.habitree.database;

import android.provider.BaseColumns;

public final class HabitContract {
    private HabitContract() {}

    /* Inner class that defines the table contents */
    public static class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "habits";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DAYS_COMPLETED = "days_completed";
        public static final String COLUMN_NAME_TAGS = "tags";
        public static final String COLUMN_NAME_TYPE = "type"; // daily -> 0/ weekly -> target # of days
    }
}
