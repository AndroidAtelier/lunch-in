package com.github.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.github.androidatelier.lunchin.LunchInDetector;
import com.github.androidatelier.lunchin.receiver.LunchInUiReceiver;
import com.github.androidatelier.lunchin.settings.SettingsAccess;
import com.github.androidatelier.lunchin.util.Constants;

public class TimePickerDialogFragment extends DialogFragment {
    public static TimePickerDialogFragment newInstance(int hours, int minutes) {
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_HOURS, hours);
        args.putInt(Constants.KEY_MINUTES, minutes);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hours, int minutes) {
                SettingsAccess settingsAccess = new SettingsAccess(getActivity());

                Intent data = new Intent();
                data.putExtra(Constants.KEY_HOURS, hours);
                data.putExtra(Constants.KEY_MINUTES, minutes);
                if (getTargetRequestCode() == Constants.REQUEST_CODE_LUNCH_START_DIALOG) {
                    settingsAccess.setLunchStartTime(hours, minutes);
                } else if (getTargetRequestCode() == Constants.REQUEST_CODE_LUNCH_END_DIALOG) {
                    settingsAccess.setLunchEndTime(hours, minutes);
                    LunchInDetector.setAlarm(getActivity(), LunchInUiReceiver.class);
                }
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
            }
        };

        int hours = getArguments().getInt(Constants.KEY_HOURS);
        int minutes = getArguments().getInt(Constants.KEY_MINUTES);
        TimePickerDialog dialog = new TimePickerDialog(
                getActivity(), listener,
                hours, minutes, DateFormat.is24HourFormat(getActivity()));

        return dialog;
    }
}