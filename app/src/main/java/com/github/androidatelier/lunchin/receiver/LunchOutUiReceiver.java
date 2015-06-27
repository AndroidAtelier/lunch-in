package com.github.androidatelier.lunchin.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.github.androidatelier.lunchin.LunchOutDetectionReceiver;
import com.github.androidatelier.lunchin.activity.MainActivity;
import com.github.androidatelier.lunchin.settings.SettingsAccess;
import com.github.androidatelier.lunchin.notification.NotificationUtil;

/**
 * Created by Kelly Shuster on 6/17/15.
 */
public class LunchOutUiReceiver extends LunchOutDetectionReceiver {

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("KIO", "app: onReceive");
        mContext = context;
        SettingsAccess sa = new SettingsAccess(context);
        updateUserSettings(sa.getWorkWifiId(), SettingsAccess.getTimeString(sa.getLunchStartTimeMinutes()), SettingsAccess.getTimeString(sa.getLunchEndTimeMinutes()), sa.getLastSSIDValue());

        super.onReceive(context, intent);
    }

    @Override
    public void onPossibleLunchOut(Context context) {
        Log.d("KIO", "app: sendNotification");
        NotificationUtil.showLunchOutNotification(context, MainActivity.class);
    }

    @Override
    public void updateLastSSID(String ssid) {
        SettingsAccess sa = new SettingsAccess(mContext);
        sa.setLastSSID(ssid);
    }
}