package com.github.androidatelier.lunchin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.github.androidatelier.database.LunchInApi;
import org.joda.time.DateTimeComparator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by A596969 on 6/22/15.
 */
public class LunchInDetectionReceiver extends BroadcastReceiver {

    private static final String KEY_LUNCH_START = "pref_lunch_start";
    public static final String KEY_PREFERENCE_FILE_SETTINGS = "com.github.androidatelier.lunchin.PREFERENCE_FILE_SETTINGS";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MARK", "in detect receiver");

        //check if lunch in should fire
        Toast.makeText(context, "checking for lunch in", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = context.getSharedPreferences(KEY_PREFERENCE_FILE_SETTINGS, Context.MODE_PRIVATE);
        String end = prefs.getString(KEY_LUNCH_START, "13:00");
        Log.d("MARK", "end: " + end);
        Date dEnd = null;
        try {
            dEnd = new SimpleDateFormat("HH:mm").parse(end);
        } catch(java.text.ParseException e) {
            Log.d("MARK", "parse failure");
            //TODO: this keeps happening, get actual end time instead of "" ^
        }
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(dEnd);

        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(new Date());

        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();

        //if time only component of endCal is < time only component of nowCal
        //and user has not yet lunched out today
        if(comparator.compare(endCal.getTime(), nowCal.getTime()) < 0 && ! (new LunchInApi(context).didUserLunchOutToday()) ) {
            Toast.makeText(context, "fire lunch in", Toast.LENGTH_SHORT).show();
            Log.d("MARK", "fire lunch in");
        } else {
            Toast.makeText(context, "not a lunch in, try again in an hour", Toast.LENGTH_SHORT).show();
            Log.d("MARK", "not lunch in");
        }

    }


}
