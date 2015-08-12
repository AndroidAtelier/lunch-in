package com.androidatelier.lunchin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.androidatelier.lunchin.utils.LunchInDetector;

public class LunchInDetectionBootStarterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            LunchInDetector.setAlarm(context, LunchInDetectionReceiver.class);
        }
    }
}