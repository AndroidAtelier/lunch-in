package com.github.androidatelier.lunchin.model;

import android.content.Context;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.Formatter;


public class Setting {
    private Context mContext;
    private SettingsAccess mSettingsAccess;
    private Resource mEnum;

    public enum Resource {
                            // key, title, description
        WORK_WIFI           (R.string.pref_key_work_wifi, R.string.pref_title_work_wifi, R.string.pref_description_work_wifi),
        LUNCH_START         (R.string.pref_key_lunch_start, R.string.pref_title_lunch_start, R.string.pref_description_lunch_start),
        LUNCH_END           (R.string.pref_key_lunch_end, R.string.pref_title_lunch_end, R.string.pref_description_lunch_end),
        LUNCH_DAYS_TRACKED  (R.string.pref_key_lunch_days_tracked, R.string.pref_title_lunch_days_tracked, R.string.pref_description_lunch_days_tracked),
        LUNCH_AVG_COST      (R.string.pref_key_avg_lunch_cost, R.string.pref_title_avg_lunch_cost, R.string.pref_description_avg_lunch_cost),
        GROSS_SALARY        (R.string.pref_key_gross_salary, R.string.pref_title_gross_salary, R.string.pref_description_gross_salary),
        SAVINGS_GOAL_NAME   (R.string.pref_key_savings_goal_name, R.string.pref_title_my_goal, R.string.pref_description_my_goal),
        SAVINGS_GOAL_VALUE  (R.string.pref_key_savings_goal_value, R.string.pref_title_my_goal, R.string.pref_description_my_goal),
        MY_GOAL             (0, R.string.pref_title_my_goal, R.string.pref_description_my_goal)
        ;

        private final int keyResource;
        private final int titleResource;
        private final int descriptionResource;

        Resource(int keyResource, int titleResource, int descriptionResource) {
            this.keyResource = keyResource;
            this.titleResource = titleResource;
            this.descriptionResource = descriptionResource;
        }

        public int getKey() {
            return this.keyResource;
        }

        public int getTitle() {
            return this.titleResource;
        }

        public int getDescription() {
            return this.descriptionResource;
        }

        public String getTitleText(Context context) {
            return context.getString(getTitle());
        }

        public String getDescriptionText(Context context) {
            return context.getString(getDescription());
        }

    }




    private Setting() {};
    public Setting(Context context, Resource settingResourceEnum) {
        this.mContext = context;
        this.mEnum = settingResourceEnum;
        mSettingsAccess = new SettingsAccess(context);
    }

    public String getTitle() {
        return mEnum.getTitleText(mContext);
    }

    public String getDescription() {
        String description = mEnum.getDescriptionText(mContext);
        Object value;
        if (!(mEnum == Resource.MY_GOAL)) {
            value = getValue(mEnum);
        } else {
            // set both SAVINGS_GOAL_NAME and SAVINGS_GOAL_VALUE  in the description
            String name =  getValue(Resource.SAVINGS_GOAL_NAME).toString();
            int cost = (int) getValue(Resource.SAVINGS_GOAL_VALUE);
            value = Formatter.formatGoal(name, cost);
        }

        if (value != null && !value.toString().isEmpty()) {
            description = value.toString();
        }
        return description;
    }

    public Object getValue(Resource resourceEnum) {
        Formatter formatter = new Formatter(mContext);
        switch (resourceEnum) {
            case WORK_WIFI :
                return mSettingsAccess.getWorkWifiId();
            case LUNCH_START :
                int[] lunchStart = mSettingsAccess.getLunchStartTime();
                int startHour = lunchStart[0];
                int startMins = lunchStart[1];
                return formatter.formatTime(startHour, startMins);
            case LUNCH_END :
                int[] lunchEnd = mSettingsAccess.getLunchEndTime();
                int endHour = lunchEnd[0];
                int endMins = lunchEnd[1];
                return formatter.formatTime(endHour, endMins);
            case LUNCH_DAYS_TRACKED :
                return mSettingsAccess.getDaysToTrack();
            case LUNCH_AVG_COST :
                double lunchCost = mSettingsAccess.getAverageLunchCost();
                return Formatter.formatDoubleToCurrencyUSD(lunchCost);
            case GROSS_SALARY :
                String value = "";
                int salary = mSettingsAccess.getGrossAnnualSalary();
                if (salary == Constants.DEFAULT_GROSS_ANNUAL_SALARY) {
                    value = Formatter.formatIntToCurrencyUSD(salary) +
                            " is the current national average per person in the US. " +
                            "Enter your information for personalized statistics. " +
                            "All settings data are stored locally and never shared";
                } else {
                    value = Formatter.formatIntToCurrencyUSD(salary);
                }
                return value;
            case SAVINGS_GOAL_NAME :
                return mSettingsAccess.getSavingsGoalName();
            case SAVINGS_GOAL_VALUE :
                return mSettingsAccess.getSavingsGoalValue();
            default : return false;
        }
    }
}
