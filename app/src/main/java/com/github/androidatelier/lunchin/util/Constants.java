package com.github.androidatelier.lunchin.util;

public abstract class Constants {



    public static final String KEY_DAYS_TO_TRACK = "daysToTrack";
    public static final String KEY_GOAL_COST = "goalCost";
    public static final String KEY_GOAL_NAME = "goalTitle";
    public static final String KEY_HOURS = "hours";
    public static final String KEY_MINUTES = "minutes";
    public static final String KEY_NETWORK = "network";
    public static final String KEY_NETWORKS = "networks";
    public static final String KEY_TEXT = "text";
    public static final String KEY_TITLE = "title";

    public static final String FRAGMENT_TAG_DAYS_TO_TRACK_DIALOG = "Days To Track";
    public static final String FRAGMENT_TAG_EDIT_TEXT_DIALOG = "EditText";
    public static final String FRAGMENT_TAG_GOAL_SETTER_DIALOG = "Goal Setter";
    public static final String FRAGMENT_TAG_TIME_PICKER_DIALOG = "Time Picker";
    public static final String FRAGMENT_TAG_WIFI_NETWORKS_DIALOG = "Wifi Networks";

    public static final int REQUEST_CODE_WIFI_NETWORKS_DIALOG = 10000;
    public static final int REQUEST_CODE_DAYS_TO_TRACK_DIALOG = 10001;
    public static final int REQUEST_CODE_LUNCH_START_DIALOG = 10002;
    public static final int REQUEST_CODE_LUNCH_END_DIALOG = 10003;
    public static final int REQUEST_CODE_LUNCH_COST_DIALOG = 10004;
    public static final int REQUEST_CODE_GOAL_SETTER_DIALOG = 10005;
    public static final int REQUEST_CODE_GROSS_SALARY_DIALOG = 10006;

    public static final int DEFAULT_WORK_WEEK = 2 + 4 + 8 + 16 + 32;  // Monday through Friday
    public static final int DEFAULT_LUNCH_BEGIN_HOURS = 12;
    public static final int DEFAULT_LUNCH_BEGIN_MINUTES = 0;
    public static final int DEFAULT_GROSS_ANNUAL_SALARY = 26695;
}