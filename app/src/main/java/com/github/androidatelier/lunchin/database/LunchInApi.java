package com.github.androidatelier.lunchin.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Jenny on 6/16/2015.
 */
public class LunchInApi {
    private final SQLiteDatabase mDb;
    private final Clock mClock;

    public LunchInApi(Context context) {
        DatabaseUtility databaseUtility = new DatabaseUtility(context);
        mDb = databaseUtility.getWritableDatabase();
        mClock = new Clock();
    }

    public LunchInApi(Context context, Clock clock, String name) {
        DatabaseUtility databaseUtility = new DatabaseUtility(context, name);
        mDb = databaseUtility.getWritableDatabase();
        mClock = clock;
    }

    /**
     * @return the number of lunch ins the person has done
     */
    public int getNumberOfLunchIns() {
        return cupboard().withDatabase(mDb)
                .query(MealItem.class)
                .withSelection("success = ?", "1")
                .getCursor()
                .getCount();
    }

    public int getNumberOfLunchOuts() {
        return cupboard().withDatabase(mDb)
                .query(MealItem.class)
                .withSelection("success = ?", "0")
                .getCursor()
                .getCount();
    }

    public int getLunchTotal() {
        return cupboard().withDatabase(mDb)
                .query(MealItem.class)
                .getCursor()
                .getCount();
    }

    public int getNumberOfLunchInsThisMonth() {
        return getNumberOfLunchsThisMonth(true);
    }

    public int getNumberOfLunchOutsThisMonth() {
        return getNumberOfLunchsThisMonth(false);
    }

    public boolean didUserLunchOutToday() {
        DateTime now = mClock.getNow();
        DateTime start = now.withTimeAtStartOfDay();
        DateTime end = start.plusDays(1);
        int count = cupboard().withDatabase(mDb)
                .query(MealItem.class)
                .withSelection("timestamp >= ? AND timestamp < ? AND success = ?",
                        String.valueOf(start.getMillis()),
                        String.valueOf(end.getMillis()),
                        "0")
                .getCursor()
                .getCount();
        return (count > 0);
    }

    /**
     * Creates another lunch in line in database
     */
    public void setLunchIn() {
        createMealItem(true);
    }

    /**
     * Indicates user went out to lunch instead of eating in
     */
    public void setLunchOut() {
        createMealItem(false);
    }

    private void createMealItem(boolean success) {
        long now = mClock.getNow().getMillis();
        MealItem item = new MealItem(now, success);
        cupboard().withDatabase(mDb).put(item);
    }

    public int getNumberOfLunchsThisMonth(boolean success) {
        DateTime now = mClock.getNow();
        DateTime start = now.withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime end = start.plusMonths(1);
        return cupboard().withDatabase(mDb)
                .query(MealItem.class)
                .withSelection("timestamp >= ? AND timestamp < ? AND success = ?",
                        String.valueOf(start.getMillis()),
                        String.valueOf(end.getMillis()),
                        success ? "1" : "0")
                .getCursor()
                .getCount();
    }
}