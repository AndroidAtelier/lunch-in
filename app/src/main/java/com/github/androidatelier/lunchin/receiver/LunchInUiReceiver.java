package com.github.androidatelier.lunchin.receiver;

import android.content.Context;

import com.github.androidatelier.lunchin.LunchInDetectionReceiver;
import com.github.androidatelier.lunchin.activity.MainActivity;
import com.github.androidatelier.lunchin.notification.NotificationUtil;

public class LunchInUiReceiver extends LunchInDetectionReceiver {
    @Override
    public void onPossibleLunchIn(Context context) {
        NotificationUtil.showLunchInNotification(context, MainActivity.class);
    }
}