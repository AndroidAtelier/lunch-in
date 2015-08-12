package com.androidatelier.lunchin.database;

public class MealItem {
    public Long _id;
    public Long timestamp;
    public boolean success;

    public MealItem() {
    }

    public MealItem(Long timestamp, boolean success) {
        this.timestamp = timestamp;
        this.success = success;
    }
}