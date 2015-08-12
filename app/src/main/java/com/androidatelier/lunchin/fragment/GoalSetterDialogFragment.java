package com.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.androidatelier.lunchin.R;
import com.androidatelier.lunchin.settings.SettingsAccess;
import com.androidatelier.lunchin.util.Constants;

public class GoalSetterDialogFragment extends DialogFragment {
    public static GoalSetterDialogFragment newInstance(String goalName, int goalCost) {
        GoalSetterDialogFragment fragment = new GoalSetterDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_GOAL_NAME, goalName);
        args.putInt(Constants.KEY_GOAL_COST, goalCost);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_goal, null);
        final TextView nameView = (TextView) view.findViewById(R.id.dialog_goal_name);
        String goalName = getArguments().getString(Constants.KEY_GOAL_NAME);
        nameView.setText(goalName);
        final TextView costView = (TextView) view.findViewById(R.id.dialog_goal_cost);
        int goalCost = getArguments().getInt(Constants.KEY_GOAL_COST);
        costView.setText(String.valueOf(goalCost));
        builder.setView(view);

        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int resultCode = Activity.RESULT_CANCELED;

                Intent data = new Intent();
                data.replaceExtras(getArguments());

                String inputtedGoalName = nameView.getText().toString();
                if (!TextUtils.isEmpty(inputtedGoalName)) {
                    data.putExtra(Constants.KEY_GOAL_NAME, inputtedGoalName);
                    resultCode = Activity.RESULT_OK;
                    // store SharedPreference
                    new SettingsAccess(getActivity()).setSavingsGoalName(inputtedGoalName);
                }
                CharSequence inputtedGoalCost = costView.getText();
                try {
                    int cost = Integer.parseInt(inputtedGoalCost.toString());
                    data.putExtra(Constants.KEY_GOAL_COST, cost);
                    resultCode = Activity.RESULT_OK;
                    // store SharedPreference
                    new SettingsAccess(getActivity()).setSavingsGoalValue(cost);
                } catch (NumberFormatException e) {
                }

                getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);

        return builder.create();
    }
}