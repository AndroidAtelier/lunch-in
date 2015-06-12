package com.github.androidatelier.lunchin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by A596969 on 6/11/15.
 *
 * TODO: Come back and deal with possibility of setting which days of week person works, or allow person to turn off notifications for x days
 * TODO: Normalize multiple SSID change receipts so only one call back to app
 */
public class LunchOutDetectionService extends android.app.Service {

    String mWorkSSID;
    String mStartTimeString; //HH:mm
    String mEndTimeString; //HH:mm
    LunchOutDetectionListener mListener;
    Context mContext;
    String mLastSSID = "";
    String mCurrentSSID = "";

    public LunchOutDetectionService() {

    }

    public LunchOutDetectionService(Context context, LunchOutDetectionListener listener, String ssid, String start, String end) {

        mWorkSSID = ssid;
        mStartTimeString = start;
        mEndTimeString = end;
        mListener = listener;
        mContext = context;

    }

    public void start() {

        Log.d("MARK", "service is starting");

        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO: Put logic block here to test wifi name and times
                WifiManager wifiManager = (WifiManager) context.getSystemService (Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo ();
                String ssid = info.getSSID().replace("\"", ""); //remove surrounding quotes

                if(mCurrentSSID != null) {
                    mLastSSID = mCurrentSSID;
                }
                mCurrentSSID = ssid;

                Log.d("MARK", ssid);

                if(isNowLunchTime(mStartTimeString, mEndTimeString)) {
                    if(isSSIDAway()) {

                        mListener.possibleLunchOutDetected();

                    }
                }
            }
        };

        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        mContext.registerReceiver(myReceiver, filters);

//        return super.startService(service);

    }

    /**
     * note that HH means hours in military time, eg 5PM = 17
     *
     * @param start HH:mm format string for start of lunch time
     * @param end HH:mm string for end of lunch time
     * @return true if current hour and minute are within range
     */
    boolean isNowLunchTime(String start, String end) {
        Date dStart;
        Date dEnd;
        try {
            dStart = new SimpleDateFormat("HH:mm").parse(start);
            dEnd = new SimpleDateFormat("HH:mm").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
            return false; //poorly formatted input
        }

        Date now = new Date();

        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        if( comparator.compare(now, dStart) >= 0 && comparator.compare(now, dEnd) <= 0) {
            Log.d("MARK", "isNowLunchTime() is false");
            return true;
        }

        Log.d("MARK", "isNowLunchTime() is true");
        return false;
    }

    boolean isSSIDAway() {
        //possibility if coming back to do normalization instead of quick and dirty way:
        //http://commons.apache.org/proper/commons-collections/javadocs/api-release/org/apache/commons/collections4/queue/CircularFifoQueue.html

        if( mLastSSID.equals( mWorkSSID ) && !mCurrentSSID.equals( mWorkSSID ) ) {
            Log.d("MARK", "isSSIDAway() is true");
            return true;
        } else {
            Log.d("MARK", "isSSIDAway() is false");
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
