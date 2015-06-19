package com.github.androidatelier.lunchin.lunch_out_detection_playground;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.androidatelier.lunchin.LunchOutDetectionReceiver;
import com.github.androidatelier.lunchin.notification.NotificationUtil;

/**
 * Created by Kelly Shuster on 6/17/15.
 */
public class LunchOutUiReceiver extends LunchOutDetectionReceiver {
    @Override
    public void onPossibleLunchOut(Context context) {
        Log.d("KIO", "Inside playground onPossibleLunchOut of LunchOutUiReceiver");
        NotificationUtil.showLunchOutNotification(context, LunchOutUiReceiver.class);
    }
}