package com.androidatelier.lunchin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.androidatelier.lunchin.activity.MainActivity;
import com.androidatelier.lunchin.database.LunchInApi;
import com.androidatelier.lunchin.notification.NotificationUtil;
import com.androidatelier.lunchin.settings.SettingsAccess;
import com.androidatelier.lunchin.util.DaysOfTheWeek;

import org.joda.time.DateTime;

public class LunchInDetectionReceiver extends BroadcastReceiver {

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

    public void onPossibleLunchIn(Context context) {
        NotificationUtil.showLunchInNotification(context, MainActivity.class);
    }
}