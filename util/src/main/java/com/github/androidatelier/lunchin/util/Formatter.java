package com.github.androidatelier.lunchin.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class Formatter {
    private static String formatLunchHour(int hours, int minutes, boolean is24HourFormat) {
        return is24HourFormat ?
                String.format("%02d:%02d", hours, minutes) :
                String.format("%d:%02d", convertTo12Hour(hours), minutes);
    }

    public static String formatTime(int hours, int minutes, boolean is24HourFormat) {
        String time = formatLunchHour(hours, minutes, is24HourFormat);
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

    /**
     * Formats a given number to the hundreths (two decimal places)
     * @param number  must be an instance of some number type ie (int, float, double)
     * @return
     */
    public static String formatToDecimalHundreths(Object number) {
        DecimalFormat df = new DecimalFormat("#.00");
        String formatted = df.format(number);
        return formatted;
    }
}
