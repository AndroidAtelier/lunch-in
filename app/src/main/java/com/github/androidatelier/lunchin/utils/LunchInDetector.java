package com.github.androidatelier.lunchin.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.github.androidatelier.lunchin.settings.SettingsAccess;
import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.Formatter;

import org.joda.time.DateTime;

public abstract class LunchInDetector {
    // Ask if user has lunched in every day at the same time.
    // Change to DateUtils.MINUTE_IN_MILLIS for debugging.
    private static final long INTERVAL = DateUtils.DAY_IN_MILLIS;

    public static void setAlarm(Context context, Class<?> receiverClass) {
        SettingsAccess settingsAccess = new SettingsAccess(context);
        if (TextUtils.isEmpty(settingsAccess.getWorkWifiId())) {
            return;
        }

        DateTime endTime = getEndTime(settingsAccess);

        PendingIntent pendingIntent = getPendingIntent(context, receiverClass);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                endTime.getMillis(),
                INTERVAL,
                pendingIntent);
    }

    private static DateTime getEndTime(SettingsAccess settingsAccess) {
        int endTimeMinutes = settingsAccess.getLunchEndTimeMinutes();
        int[] hoursAndMinutes = Formatter.getHoursAndMinutes(endTimeMinutes);
        int hours = hoursAndMinutes[0];
        int minutes = hoursAndMinutes[1];

        DateTime now = new DateTime();
        DateTime endTime = now
                .withHourOfDay(hours)
                .withMinuteOfHour(minutes)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);

        if (endTime.isBefore(now)) {
            endTime = endTime.plusDays(1);
        }

        return endTime;
    }

    private static PendingIntent getPendingIntent(Context context, Class<?> receiverClass) {
        Intent alarmIntent = new Intent(context, receiverClass);
        return PendingIntent.getBroadcast(
                context, Constants.REQUEST_CODE_LUNCH_END_BROADCAST, alarmIntent, 0);
    }
}