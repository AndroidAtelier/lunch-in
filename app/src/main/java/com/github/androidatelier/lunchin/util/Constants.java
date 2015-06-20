package com.github.androidatelier.lunchin.util;

public abstract class Constants {
  public static final String KEY_DAYS_TO_TRACK = "daysToTrack";
  public static final String KEY_GOAL_COST = "goalCost";
  public static final String KEY_GOAL_NAME = "goalTitle";
  public static final String KEY_NETWORK = "network";
  public static final String KEY_NETWORKS = "networks";

  public static final String FRAGMENT_TAG_WIFI_NETWORKS_DIALOG = "Wifi Networks";
  public static final String FRAGMENT_TAG_GOAL_SETTER_DIALOG = "Goal Setter";
  public static final String FRAGMENT_TAG_DAYS_TO_TRACK_DIALOG = "Days To Track";

  public static final int REQUEST_CODE_WIFI_NETWORKS_DIALOG = 10000;
  public static final int REQUEST_CODE_GOAL_SETTER_DIALOG = 10001;
  public static final int REQUEST_CODE_DAYS_TO_TRACK_DIALOG = 10002;

  public static final int DEFAULT_WORK_WEEK = 2 + 4 + 8 + 16 + 32;  // Monday through Friday
}