package com.github.androidatelier.lunchin.lunch_out_detection_playground;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.androidatelier.lunchin.LunchOutDetectionReceiver;

/**
 * Created by Kelly Shuster on 6/17/15.
 */
public class LunchOutUiReceiver extends LunchOutDetectionReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context,intent);
    }

    @Override
    public void onPossibleLunchOut() {
        Log.d("KIO", "Inside playground onPossibleLunchOut of LunchOutUiReceiver");
    }
}
