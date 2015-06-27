package com.github.androidatelier.lunchin.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.github.androidatelier.database.LunchInApi;
import com.github.androidatelier.lunchin.LunchOutDetectionReceiver;
import com.github.androidatelier.lunchin.activity.MainActivity;
import com.github.androidatelier.lunchin.notification.NotificationUtil;

/**
 * Created by Kelly Shuster on 6/17/15.
 */
public class LunchOutUiReceiver extends LunchOutDetectionReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("KIO", "app: onRecieve");
        new LunchInApi(context).didUserLunchOutToday();
        super.onReceive(context, intent);
    }

    @Override
    public void onPossibleLunchOut(Context context) {
        Log.d("KIO", "app: sendNotification");
        NotificationUtil.showLunchOutNotification(context, MainActivity.class);
    }

    @Override
    public void updateLastSSID(String ssid) {

    }
}