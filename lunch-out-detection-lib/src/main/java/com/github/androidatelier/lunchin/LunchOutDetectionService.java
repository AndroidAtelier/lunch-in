package com.github.androidatelier.lunchin;

import android.content.*;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by A596969 on 6/11/15.
 */
public class LunchOutDetectionService extends android.app.Service {

    String WORK_SSID;
    int startTime;
    int endTime;
    LunchOutDetectionListener mListener;
    Context mContext;

    public LunchOutDetectionService() {

    }

    public LunchOutDetectionService(Context context, LunchOutDetectionListener listener, String ssid, int start, int end) {

        WORK_SSID = ssid;
        startTime = start;
        endTime = end;
        mListener = listener;
        mContext = context;

    }

    public void start() {



        Log.d("MARK", "service is starting");

        //this is fake, move inside logic block soon
        mListener.possibleLunchOutDetected();

        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO: Put logic block here to test wifi name and times
                WifiManager wifiManager = (WifiManager) context.getSystemService (Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo ();
                String ssid = info.getSSID();
                Log.d("MARK", ssid);
            }
        };

        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        mContext.registerReceiver(myReceiver, filters);

//        return super.startService(service);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
