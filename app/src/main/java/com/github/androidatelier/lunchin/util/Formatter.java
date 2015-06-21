package com.github.androidatelier.lunchin.util;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by brenda on 6/20/15.
 */
public class Formatter {

    Context mContext;


    public Formatter(Context context) {
        this.mContext = context;
    }

    private String formatLunchHour(int hours, int minutes) {
        return DateFormat.is24HourFormat(mContext) ?
                String.format("%02d:%02d", hours, minutes) :
                String.format("%d:%02d", convertTo12Hour(hours), minutes);
    }

    public String formatTime(int hours, int minutes) {
        String time = formatLunchHour(hours, minutes);
        if (hours < 12) {
            time += " AM";
        } else {
            time += " PM";
        }
        return time;
    }

    public static int convertTo12Hour(int hour) {
        int result = hour % 12;
        return (result == 0) ? 12 : result;
    }


    public static String formatGoal(String goalName, int goalCost) {
        String formatted = goalName + ": " + formatIntToCurrencyUSD(goalCost);
        return formatted;
    }


    public static int[] getHoursAndMinutes(int timeInMinutes) {
        int hour = 0;
        int minutes = 0;
        if (timeInMinutes > 0) {
            hour = timeInMinutes / 60;
            minutes = timeInMinutes % 60;
        }
        int[] values = new int[2];
        values[0] = hour;
        values[1] = minutes;
        return values;
    }

    public static String formatIntToCurrencyUSD(int intVal) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        return numberFormat.format((double)intVal);
    }

    public static String formatDoubleToCurrencyUSD(double doubleVal) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        return numberFormat.format(doubleVal);
    }

}
