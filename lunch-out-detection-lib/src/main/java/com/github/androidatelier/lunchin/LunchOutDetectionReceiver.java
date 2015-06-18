package com.github.androidatelier.lunchin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mark S on 6/11/15.
 *
 * TODO: Come back and deal with possibility of setting which days of week person works, or allow person to turn off notifications for x days
 * TODO: Normalize multiple SSID change receipts so only one call back to app
 */
public class LunchOutDetectionReceiver extends BroadcastReceiver {

    public static final String PREFS= "com.github.androidatelier.lunchin.lunch_out_detection.prefs";
    public static final String WORK_WIFI= "com.github.androidatelier.lunchin.lunch_out_detection.work_wifi";
    public static final String START_TIME= "com.github.androidatelier.lunchin.lunch_out_detection.start_time";
    public static final String END_TIME= "com.github.androidatelier.lunchin.lunch_out_detection.end_time";

    private String mWorkSSID;
    private String mStartTimeString; //HH:mm
    private String mEndTimeString; //HH:mm
    private String mLastSSID = "";
    private String mCurrentSSID = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("KIO", "LunchOutDetectionReceiver::onReceive()");

        // TODO: Remove this call once we have real data from shared prefs. Until now, send notification
        //    on any wifi state change
        sendNotification();

        updateUserSettings(context);

        if( mWorkSSID!= null && mStartTimeString != null && mEndTimeString!= null) {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            String ssid = info.getSSID().replace("\"", ""); //remove surrounding quotes

            if (mCurrentSSID != null) {
                mLastSSID = mCurrentSSID;
            }
            mCurrentSSID = ssid;

            Log.d("MARK", ssid);

            if (isNowLunchTime(mStartTimeString, mEndTimeString)) {
                if (isSSIDAway()) {
                    Log.d("KIO", "sendNotification is called");
                    sendNotification();
                }
            }
        }
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

    void updateUserSettings(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        mWorkSSID = prefs.getString(WORK_WIFI, "");
        mStartTimeString = prefs.getString(START_TIME, "");
        mEndTimeString = prefs.getString(END_TIME, "");
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

    public void sendNotification() {
        // Does nothing until UI implements it
    }

}
