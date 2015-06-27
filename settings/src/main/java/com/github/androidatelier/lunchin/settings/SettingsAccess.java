package com.github.androidatelier.lunchin.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.DaysOfTheWeek;
import com.github.androidatelier.lunchin.util.Formatter;

/**
 * SettingsAccess provides mutator and accessor methods for the SharedPreferences data
 * @see Setting
 */
public class SettingsAccess {
    // default values
    private static final double DEFAULT_AVG_LUNCH_COST = 10.00;
    private static final int DEFAULT_LUNCH_START = 720; // represented in minutes
    private static final int DEFAULT_LUNCH_END = 780; // represented in minutes
    private static final String KEY_PREFERENCE_FILE_SETTINGS = "com.github.androidatelier.lunchin.PREFERENCE_FILE_SETTINGS";

    private Context mContext;
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;


    private SettingsAccess() {}
    public SettingsAccess(Context context) {
        this.mContext = context;
        mSharedPreference = mContext.getSharedPreferences(KEY_PREFERENCE_FILE_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSharedPreference.edit();
    }

    public SharedPreferences getSettingsPreference() {
        return mSharedPreference;
    }

    /**
     * Sets the work wifi ssid
     * @param ssid
     * @return true if saved to persistent storage, false otherwise
     */
    public boolean setWorkWifiId(String ssid) {
        mEditor.putString(Setting.Resource.WORK_WIFI.getKey(), ssid);
        boolean committed = mEditor.commit();
        return committed;
    }

    public String getWorkWifiId() {
        return mSharedPreference.getString(Setting.Resource.WORK_WIFI.getKey(), "");
    }

    /**
     *
     * @return
     */
    public boolean setDaysToTrack(int daysToTrack) {
        mEditor.putInt(Setting.Resource.LUNCH_DAYS_TRACKED.getKey(), daysToTrack);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * default value if not user set is M-F
     */
    public DaysOfTheWeek getDaysToTrack() {
        int daysTracked = mSharedPreference.getInt(Setting.Resource.LUNCH_DAYS_TRACKED.getKey(), Constants.DEFAULT_WORK_WEEK);

        final DaysOfTheWeek daysToTrack = new DaysOfTheWeek(
                mContext.getResources().getStringArray(R.array.days_of_the_week));
        daysToTrack.bitVector = daysTracked;

        return daysToTrack;
    }

    /**
     *
     * @return
     */
    public boolean setLunchStartTime(int hourIn24HourFormat, int minutes) {
        int timeInMinutes = hourIn24HourFormat * 60 + minutes;
        mEditor.putInt(Setting.Resource.LUNCH_START.getKey(), timeInMinutes);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * default value if not user set is 12:00 noon
     * @return
     */
    public int[] getLunchStartTime() {
        int timeInMinutes = mSharedPreference.getInt(Setting.Resource.LUNCH_START.getKey(), DEFAULT_LUNCH_START);
        return Formatter.getHoursAndMinutes(timeInMinutes);
    }

    public int getLunchStartTimeMinutes() {
        return mSharedPreference.getInt(Setting.Resource.LUNCH_START.getKey(), DEFAULT_LUNCH_START);
    }

    public String getLunchStartTimeString() {
        int timeInMinutes = mSharedPreference.getInt(Setting.Resource.LUNCH_START.getKey(), DEFAULT_LUNCH_START);
        int[] hoursAndMinutes = Formatter.getHoursAndMinutes(timeInMinutes);
        return String.format("%d:%02d", hoursAndMinutes[0], hoursAndMinutes[1]);
    }


    /**
     * @return
     */
    public boolean setLunchEndTime(int hourIn24HourFormat, int minutes) {
        int timeInMinutes = hourIn24HourFormat * 60 + minutes;
        mEditor.putInt(Setting.Resource.LUNCH_END.getKey(), timeInMinutes);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * default value if not user set is 1:00pm
     * @return
     */
    public int[] getLunchEndTime() {
        int timeInMinutes =  mSharedPreference.getInt(Setting.Resource.LUNCH_END.getKey(), DEFAULT_LUNCH_END);
        return Formatter.getHoursAndMinutes(timeInMinutes);
    }

    public int getLunchEndTimeMinutes() {
        return mSharedPreference.getInt(Setting.Resource.LUNCH_END.getKey(), DEFAULT_LUNCH_END);
    }

    /**
     * Helper method, takes int representation of time and returns HH:mm string
     *
     * @param timeInMinutes
     * @return
     */
    public static String getTimeString(int timeInMinutes) {
        int[] hoursAndMinutes = Formatter.getHoursAndMinutes(timeInMinutes);
        return String.format("%d:%02d", hoursAndMinutes[0], hoursAndMinutes[1]);
    }

    /**
     * Sets the average cost of lunch as provided by the user, stored as a String.
     *
     * @param averageLunchCost
     * @return
     */
    public boolean setAverageLunchCost(String averageLunchCost) {
        mEditor.putString(Setting.Resource.LUNCH_AVG_COST.getKey(), averageLunchCost);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * Gets the average lunch cost stored in persistent data and returns as double.
     * If no value stored, the return value is defaulted to 10.00
     * Note: We are already rounding and using general figures so precision loss due to double is not an issue.
     * @return
     */
    public double getAverageLunchCost() {
        double value;
        double defaultValue = DEFAULT_AVG_LUNCH_COST;
        String persistedValue = mSharedPreference.getString(Setting.Resource.LUNCH_AVG_COST.getKey(), "");
        if (!persistedValue.isEmpty()) {
            value = Double.parseDouble(persistedValue);
        } else {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Sets the user's gross annual salary as provided by the user, stored as a int.
     *
     * @param grossAnnualSalary
     * @return boolean
     */
    public boolean setGrossAnnualSalary(int grossAnnualSalary) {
        mEditor.putInt(Setting.Resource.GROSS_SALARY.getKey(), grossAnnualSalary);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * Gets the user's gross annual salary.
     * If returnDefaultValueIfNotSet and value not set by user, default value returned is the national average salary per person of
     * $26,695 current as of June 2015
     * Else if returnDefaultValueIfNotSet is false, value of Constants.CODE_GROSS_SALARY_NOT_SET is returned
     * @Param returnDefaultValueIfNotSet
     * @return int
     */
    public int getGrossAnnualSalary(boolean returnDefaultValueIfNotSet) {
        int defaultValue = Constants.DEFAULT_GROSS_ANNUAL_SALARY;
        int value;
        if (returnDefaultValueIfNotSet) {
            value = mSharedPreference.getInt(Setting.Resource.GROSS_SALARY.getKey(), defaultValue);
        } else {
            value = mSharedPreference.getInt(Setting.Resource.GROSS_SALARY.getKey(), Constants.CODE_GROSS_SALARY_NOT_SET);
        }
        return value;
    }

    public boolean setSavingsGoalName(String goalName) {
        mEditor.putString(Setting.Resource.SAVINGS_GOAL_NAME.getKey(), goalName);
        boolean committed = mEditor.commit();
        return committed;
    }

    public String getSavingsGoalName() {
        String defaultGoalName = mContext.getString(R.string.default_goal_name);
        return mSharedPreference.getString(Setting.Resource.SAVINGS_GOAL_NAME.getKey(), defaultGoalName);
    }

    public boolean setSavingsGoalValue(int goalValue) {
        mEditor.putInt(Setting.Resource.SAVINGS_GOAL_VALUE.getKey(), goalValue);
        boolean committed = mEditor.commit();
        return committed;
    }

    public int getSavingsGoalValue() {
        return mSharedPreference.getInt(Setting.Resource.SAVINGS_GOAL_VALUE.getKey(), 5000);
    }

    public boolean setLastSSID(String ssid) {
        mEditor.putString(Setting.Resource.LAST_SSID_VALUE.getKey(), ssid);
        boolean committed = mEditor.commit();
        return committed;
    }

    public String getLastSSIDValue() {
        return mSharedPreference.getString(Setting.Resource.LAST_SSID_VALUE.getKey(), "");
    }
}
