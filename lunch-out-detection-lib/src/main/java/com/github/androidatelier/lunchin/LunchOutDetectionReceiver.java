package com.github.androidatelier.lunchin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mark S on 6/11/15.
 *
 * TODO: Come back and deal with possibility of setting which days of week person works, or allow person to turn off notifications for x days
 * TODO: Normalize multiple SSID change receipts so only one call back to app
 */
public abstract class LunchOutDetectionReceiver extends BroadcastReceiver {

    public static final String PREFS= "com.github.androidatelier.lunchin.lunch_out_detection.prefs";
    public static final String WORK_WIFI= "com.github.androidatelier.lunchin.lunch_out_detection.work_wifi";
    public static final String START_TIME= "com.github.androidatelier.lunchin.lunch_out_detection.start_time";
    public static final String END_TIME= "com.github.androidatelier.lunchin.lunch_out_detection.end_time";

    private String mWorkSSID;
    private String mStartTimeString; //HH:mm
    private String mEndTimeString; //HH:mm
    private String mCurrentSSID = "";
    private String mLastSSID;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("KIO", "LunchOutDetectionReceiver::onReceive()");

        if(!mWorkSSID.isEmpty() && !mStartTimeString.isEmpty() && !mEndTimeString.isEmpty()) {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            String ssid = info.getSSID().replace("\"", ""); //remove surrounding quotes

            mCurrentSSID = ssid;

            Log.d("MARK", ssid);

            if (isNowLunchTime(mStartTimeString, mEndTimeString)) {
                if (isSSIDAway()) {
                    Log.d("KIO", "sendNotification is called");
                    onPossibleLunchOut(context);
                }
            }

            if(mLastSSID != mCurrentSSID) {
                updateLastSSID(mCurrentSSID);
                Log.d("MARK", "updating last to: " + mCurrentSSID);
                mLastSSID = mCurrentSSID;
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

        Calendar cTodayStart = getTodayCalendarFromDate(dStart);

        Calendar cTodayEnd = getTodayCalendarFromDate(dEnd);


        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        if( now.getTime() >= cTodayStart.getTime().getTime() && now.getTime()  <= cTodayEnd.getTime().getTime() ) {
            Log.d("MARK", "isNowLunchTime() is true: " + cTodayStart.getTime() + ", " + cTodayEnd.getTime() + ", " + now);
            return true;
        }
        Log.d("MARK", "isNowLunchTime() is false: " + cTodayStart.getTime() + ", " + cTodayEnd.getTime() + ", " + now);
        return false;
    }

    private Calendar getTodayCalendarFromDate(Date date) {
        Calendar incomingTime = Calendar.getInstance();
        incomingTime.setTime(date);

        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(new Date());

        todayCal.set(Calendar.HOUR_OF_DAY, incomingTime.get(Calendar.HOUR_OF_DAY));
        todayCal.set(Calendar.MINUTE, incomingTime.get(Calendar.MINUTE));
        todayCal.set(Calendar.SECOND, 0);

        return todayCal;
    }

    public void updateUserSettings(String wifi, String start, String end, String last){
        mWorkSSID = wifi;
        mStartTimeString = start;
        mEndTimeString = end;
        mLastSSID = last;
    }


    boolean isSSIDAway() {
        //possibility if coming back to do normalization instead of quick and dirty way:
        //http://commons.apache.org/proper/commons-collections/javadocs/api-release/org/apache/commons/collections4/queue/CircularFifoQueue.html

        if( mLastSSID.equals( mWorkSSID ) && !mCurrentSSID.equals( mWorkSSID ) ) {
            Log.d("MARK", "isSSIDAway() is true (last, current, work): " + mLastSSID +", " + mCurrentSSID + ", " + mWorkSSID);
            return true;
        } else {
            Log.d("MARK", "isSSIDAway() is false (last, current, work): " + mLastSSID +", " + mCurrentSSID + ", " + mWorkSSID);
            return false;
        }

    }

    public abstract void onPossibleLunchOut(Context context);

    public abstract void updateLastSSID(String ssid);
}
