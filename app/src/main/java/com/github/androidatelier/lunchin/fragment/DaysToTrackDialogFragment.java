package com.github.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.model.SettingsAccess;
import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.DaysOfTheWeek;

public class DaysToTrackDialogFragment extends DialogFragment {
    public static DaysToTrackDialogFragment newInstance(int daysToTrack) {
        DaysToTrackDialogFragment fragment = new DaysToTrackDialogFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_DAYS_TO_TRACK, daysToTrack);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final SettingsAccess settingsAccess = new SettingsAccess(getActivity());
        final DaysOfTheWeek daysToTrack = settingsAccess.getDaysToTrack();
        String[] daysOfTheWeek = getResources().getStringArray(R.array.days_of_the_week);

        boolean[] selected = new boolean[daysOfTheWeek.length];
        for (int i = 0; i < daysOfTheWeek.length; ++i) {
            selected[i] = daysToTrack.isSet(i);
        }

        builder.setMultiChoiceItems(
                daysOfTheWeek, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            daysToTrack.set(position);
                        } else {
                            daysToTrack.clear(position);
                        }
                    }
                });

        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent data = new Intent();
                data.putExtra(Constants.KEY_DAYS_TO_TRACK, daysToTrack.bitVector);
                settingsAccess.setDaysToTrack(daysToTrack.bitVector);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);

        return builder.create();
    }
}