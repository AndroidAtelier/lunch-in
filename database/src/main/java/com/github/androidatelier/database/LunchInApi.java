package com.github.androidatelier.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Jenny on 6/16/2015.
 */
public class LunchInApi {
    private final SQLiteDatabase db;

    public LunchInApi(Context context) {
        DatabaseUtility databaseUtility = new DatabaseUtility(context);
        db = databaseUtility.getWritableDatabase();
    }

    /**
     * @return the number of lunch ins the person has done
     */
    public int getNumberOfLunchIns() {
        return cupboard().withDatabase(db)
                .query(MealItem.class)
                .withSelection("success = ?", "1")
                .getCursor()
                .getCount();
    }

    public int getNumberOfLunchOuts() {
        return cupboard().withDatabase(db)
                .query(MealItem.class)
                .withSelection("success = ?", "0")
                .getCursor()
                .getCount();
    }

    public int getLunchTotal() {
        return cupboard().withDatabase(db)
                .query(MealItem.class)
                .getCursor()
                .getCount();
    }

    public boolean didUserLunchOutToday() {
        DateTime now = getNow();
        DateTime start = now.withTimeAtStartOfDay();
        DateTime end = start.plusDays(1);
        int count = cupboard().withDatabase(db)
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
        long now = getNow().getMillis();
        MealItem item = new MealItem(now, success);
        cupboard().withDatabase(db).put(item);
    }

    private DateTime getNow() {
        return new DateTime();
    }
}