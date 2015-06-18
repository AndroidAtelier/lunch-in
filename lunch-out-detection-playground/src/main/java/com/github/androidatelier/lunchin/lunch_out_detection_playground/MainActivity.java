package com.github.androidatelier.lunchin.lunch_out_detection_playground;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.github.androidatelier.lunchin.LunchOutDetectionListener;
import com.github.androidatelier.lunchin.LunchOutDetectionReceiver;

public class MainActivity extends Activity implements LunchOutDetectionListener {

    private TextView mUserWifiText;
    private TextView mStartTimeText;
    private TextView mEndTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUserPrefs();

        mUserWifiText = (TextView) findViewById(R.id.text_user_wifi_value);
        mStartTimeText = (TextView) findViewById(R.id.text_lunch_start_value);
        mEndTimeText = (TextView) findViewById(R.id.text_lunch_end_value);

        displayCurrentUserPrefs();
    }

    private void setupUserPrefs(){
        SharedPreferences.Editor editor = getSharedPreferences(
                LunchOutDetectionReceiver.PREFS, MODE_PRIVATE).edit();
        editor.putString(LunchOutDetectionReceiver.WORK_WIFI, "iWork");
        editor.putString(LunchOutDetectionReceiver.START_TIME, "20:00");
        editor.putString(LunchOutDetectionReceiver.END_TIME, "22:00");
        editor.apply();
    }

    private void displayCurrentUserPrefs(){
        SharedPreferences prefs = getSharedPreferences(LunchOutDetectionReceiver.PREFS, MODE_PRIVATE);

        mUserWifiText.setText(prefs.getString(LunchOutDetectionReceiver.WORK_WIFI, ""));
        mStartTimeText.setText(prefs.getString(LunchOutDetectionReceiver.START_TIME, ""));
        mEndTimeText.setText(prefs.getString(LunchOutDetectionReceiver.END_TIME, ""));
    }

    private void askAboutLunchPlans(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Don't do it!!");
        alertDialog.setMessage("Are you going out to lunch?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void possibleLunchOutDetected(){
        askAboutLunchPlans();
    }
}
