package com.github.androidatelier.lunchin.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.Formatter;

public class GoalReachedDialogFragment extends DialogFragment {
    public static GoalReachedDialogFragment newInstance(String goalName, int goalCost) {
        GoalReachedDialogFragment fragment = new GoalReachedDialogFragment();
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

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_goal_reached, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.dollarView);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.dollar);
        imageView.startAnimation(animation);

        final TextView nameView = (TextView) view.findViewById(R.id.dialog_goal_name);
        String goalName = getArguments().getString(Constants.KEY_GOAL_NAME);
        nameView.setText(goalName);

        final TextView costView = (TextView) view.findViewById(R.id.dialog_goal_cost);
        int goalCost = getArguments().getInt(Constants.KEY_GOAL_COST);
        costView.setText(Formatter.formatIntToCurrencyUSD(goalCost));

        builder.setView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }
}