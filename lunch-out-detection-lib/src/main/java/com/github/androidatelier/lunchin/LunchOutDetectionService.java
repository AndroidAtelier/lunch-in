package com.github.androidatelier.lunchin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by A596969 on 6/11/15.
 */
public class LunchOutDetectionService {

    public LunchOutDetectionService(Context context, LunchOutDetectionListener listener) {

        listener.possibleLunchOutDetected();

        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WifiManager wifiManager = (WifiManager) context.getSystemService (Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo ();
                String ssid = info.getSSID();
                Log.d("MARK", ssid);
            }
        };

        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        context.registerReceiver(myReceiver, filters);
    }

}
