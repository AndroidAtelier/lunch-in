package com.github.androidatelier.lunchin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.androidatelier.lunchin.LunchInDetector;

public class LunchInDetectionBootStarterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            LunchInDetector.setAlarm(context, LunchInUiReceiver.class);
        }
    }
}