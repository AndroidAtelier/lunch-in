package com.github.androidatelier.lunchin.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.androidatelier.lunchin.R;
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

    private String key_preference_file_settings;
    private String key_work_wifi;
    private String key_lunch_days_tracked;
    private String key_lunch_start;
    private String key_lunch_end;
    private String key_lunch_cost;
    private String key_gross_salary;
    private String key_savings_goal_name;
    private String key_savings_goal_value;

    private Context mContext;
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;


    private SettingsAccess() {}
    public SettingsAccess(Context context) {
        this.mContext = context;
        initializeKeys();
        mSharedPreference = mContext.getSharedPreferences(key_preference_file_settings, Context.MODE_PRIVATE);
        mEditor = mSharedPreference.edit();
    }

    public SharedPreferences getSettingsPreference() {
        return mSharedPreference;
    }

    private void initializeKeys() {
        key_preference_file_settings = mContext.getString(R.string.preference_file_settings);
        key_work_wifi = mContext.getString(Setting.Resource.WORK_WIFI.getKey());
        key_lunch_days_tracked = mContext.getString(Setting.Resource.LUNCH_DAYS_TRACKED.getKey());
        key_lunch_start = mContext.getString(Setting.Resource.LUNCH_START.getKey());
        key_lunch_end = mContext.getString(Setting.Resource.LUNCH_END.getKey());
        key_lunch_cost = mContext.getString(Setting.Resource.LUNCH_AVG_COST.getKey());
        key_gross_salary = mContext.getString(Setting.Resource.GROSS_SALARY.getKey());
        key_savings_goal_name = mContext.getString(Setting.Resource.SAVINGS_GOAL_NAME.getKey());
        key_savings_goal_value = mContext.getString(Setting.Resource.SAVINGS_GOAL_VALUE.getKey());
    }


    /**
     * Sets the work wifi ssid
     * @param ssid
     * @return true if saved to persistent storage, false otherwise
     */
    public boolean setWorkWifiId(String ssid) {
        mEditor.putString(key_work_wifi, ssid);
        boolean committed = mEditor.commit();
        return committed;
    }

    public String getWorkWifiId() {
        return mSharedPreference.getString(key_work_wifi, "");
    }

    /**
     *
     * @return
     */
    public boolean setDaysToTrack(int daysToTrack) {
        mEditor.putInt(key_lunch_days_tracked, daysToTrack);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * default value if not user set is M-F
     */
    public DaysOfTheWeek getDaysToTrack() {
        int daysTracked = mSharedPreference.getInt(key_lunch_days_tracked, Constants.DEFAULT_WORK_WEEK);

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
        mEditor.putInt(key_lunch_start, timeInMinutes);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * default value if not user set is 12:00 noon
     * @return
     */
    public int[] getLunchStartTime() {
        int timeInMinutes = mSharedPreference.getInt(key_lunch_start, DEFAULT_LUNCH_START);
        return Formatter.getHoursAndMinutes(timeInMinutes);
    }

    /**
     * @return
     */
    public boolean setLunchEndTime(int hourIn24HourFormat, int minutes) {
        int timeInMinutes = hourIn24HourFormat * 60 + minutes;
        mEditor.putInt(key_lunch_end, timeInMinutes);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * default value if not user set is 1:00pm
     * @return
     */
    public int[] getLunchEndTime() {
        int timeInMinutes =  mSharedPreference.getInt(key_lunch_end, DEFAULT_LUNCH_END);
        return Formatter.getHoursAndMinutes(timeInMinutes);
    }



    /**
     * Sets the average cost of lunch as provided by the user, stored as a String.
     *
     * @param averageLunchCost
     * @return
     */
    public boolean setAverageLunchCost(String averageLunchCost) {
        mEditor.putString(key_lunch_cost, averageLunchCost);
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
        String persistedValue = mSharedPreference.getString(key_lunch_cost, "");
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
        mEditor.putInt(key_gross_salary, grossAnnualSalary);
        boolean committed = mEditor.commit();
        return committed;
    }

    /**
     * Gets the user's gross annual salary.
     * Default value, if not set by user, is the national average salary per person of
     * $26,695 current as of June 2015
     * @return int
     */
    public int getGrossAnnualSalary() {
        int defaultValue = Constants.DEFAULT_GROSS_ANNUAL_SALARY;
        return mSharedPreference.getInt(key_gross_salary, defaultValue);
    }

    public boolean setSavingsGoalName(String goalName) {
        mEditor.putString(key_savings_goal_name, goalName);
        boolean committed = mEditor.commit();
        return committed;
    }

    public String getSavingsGoalName() {
        String defaultGoalName = mContext.getString(R.string.default_goal_name);
        return mSharedPreference.getString(key_savings_goal_name, defaultGoalName);
    }

    public boolean setSavingsGoalValue(int goalValue) {
        mEditor.putInt(key_savings_goal_value, goalValue);
        boolean committed = mEditor.commit();
        return committed;
    }

    public int getSavingsGoalValue() {
        return mSharedPreference.getInt(key_savings_goal_value, 5000);
    }
}
