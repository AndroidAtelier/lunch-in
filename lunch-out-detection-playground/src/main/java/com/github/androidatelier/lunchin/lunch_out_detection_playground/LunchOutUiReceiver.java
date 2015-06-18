package com.github.androidatelier.lunchin.lunch_out_detection_playground;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.androidatelier.lunchin.LunchOutDetectionReceiver;

/**
 * Created by Kelly Shuster on 6/17/15.
 */
public class LunchOutUiReceiver extends LunchOutDetectionReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        super.onReceive(context,intent);
    }

    @Override
    public void sendNotification() {
        Toast.makeText(mContext, "NOTIFICATION", Toast.LENGTH_SHORT).show();
    }
}
