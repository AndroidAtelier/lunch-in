package com.github.androidatelier.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jenny on 6/16/2015.
 */
public class DatabaseUtility extends SQLiteOpenHelper {
    public static final String TABLE_LUNCHIN = "LunchIn";
    public static final String COLUMN_LUNCHIN_ID = "LunchInId";
    public static final String COLUMN_LUNCHIN_DATE = "LunchInDate";
    public static final String COLUMN_LUNCHIN_SUCCESS = "Success";

    private static final String TABLE_PREFERENCES = "Preferences";
    private static final String COLUMN_AVG_LUNCH_COST = "AvgLunchCost";

    private static final String DATABASE_NAME = "lunchin.db";
    private static final int DATABASE_VERSION = 1;

    //database creation
    private static final String DATABASE_CREATE = "create table "
            + TABLE_LUNCHIN + "(" + COLUMN_LUNCHIN_ID
            + " integer primary key autoincrement, " + COLUMN_LUNCHIN_DATE
            + " datetime not null, " + COLUMN_LUNCHIN_SUCCESS
            + " bit);";

    private static final String PREFERENCES_DB_CREATE = "create table "
            + TABLE_PREFERENCES + "(" + COLUMN_AVG_LUNCH_COST
            + " INTEGER DEFAULT 0);";

    public DatabaseUtility(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_LUNCHIN);
    }
}
