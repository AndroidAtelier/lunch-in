package com.github.androidatelier.lunchin.lunch_out_detection_playground;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.androidatelier.lunchin.LunchOutDetectionListener;
import com.github.androidatelier.lunchin.LunchOutDetectionService;


public class MainActivity extends ActionBarActivity implements LunchOutDetectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dummy Alert Dialog. Will be hooked up to lib eventually
        askAboutLunchPlans();

        // Pass my interface implementation to the library
        LunchOutDetectionService newService = new LunchOutDetectionService(this);

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
