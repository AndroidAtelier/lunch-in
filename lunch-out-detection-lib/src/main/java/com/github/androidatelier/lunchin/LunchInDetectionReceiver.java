package com.github.androidatelier.lunchin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.androidatelier.database.LunchInApi;
import com.github.androidatelier.lunchin.settings.SettingsAccess;
import com.github.androidatelier.lunchin.util.DaysOfTheWeek;

import org.joda.time.DateTime;

public abstract class LunchInDetectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsAccess settingsAccess = new SettingsAccess(context);
        LunchInApi lunchInApi = new LunchInApi(context);
        DaysOfTheWeek daysOfTheWeek = settingsAccess.getDaysToTrack();

        DateTime now = new DateTime();
        boolean isWorkDay = daysOfTheWeek.isDayOfTheWeekSet(now.getDayOfWeek());
        if (isWorkDay && !lunchInApi.didUserLunchOutToday()) {
            onPossibleLunchIn(context);
        }
    }

    public abstract void onPossibleLunchIn(Context context);
}