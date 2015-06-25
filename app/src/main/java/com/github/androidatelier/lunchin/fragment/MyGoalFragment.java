package com.github.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.androidatelier.database.LunchInApi;
import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.component.ui.PieChartView;
import com.github.androidatelier.lunchin.model.SettingsAccess;
import com.github.androidatelier.lunchin.notification.NotificationUtil;
import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.Formatter;

public class MyGoalFragment extends Fragment implements Updateable {
    private static final String TAG = "MyGoalFragment";

    private SettingsAccess mSettingsAccess;
    private LunchInApi mLunchInApi;

    private TextView mMoneySacrificed;
    private TextView mGoalProgress;
    private TextView mGoalRemaining;
    private PieChartView mPieChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_my_goal,container,false);
        mMoneySacrificed = (TextView) v.findViewById(R.id.fragment_money_sacrificed_to_lunch_out);
        mGoalProgress = (TextView) v.findViewById(R.id.fragment_my_goal_progress_gained);
        mGoalRemaining = (TextView) v.findViewById(R.id.fragment_my_goal_progress_remaining);
        mPieChartView = (PieChartView) v.findViewById(R.id.pie_chart_view);

        mSettingsAccess = new SettingsAccess(getActivity());
        mLunchInApi = new LunchInApi(getActivity());

        initializeViews(v);

        update();

        String action = getActivity().getIntent().getAction();
        if (NotificationUtil.ACTION_LUNCH_OUT.equals(action)) {
            getActivity().getIntent().setAction(null);
            updateLunchOutUI();
        }
        if (NotificationUtil.ACTION_LUNCH_IN.equals(action)) {
            getActivity().getIntent().setAction(null);
            updateLunchInUI();
        }

        return v;
    }

    private void initializeViews(View v) {
        TextView goalName = (TextView) v.findViewById(R.id.fragment_my_goal_name);
        goalName.setText(mSettingsAccess.getSavingsGoalName());

        TextView goalValue = (TextView) v.findViewById(R.id.fragment_my_goal_value);
        goalValue.setText(Formatter.formatIntToCurrencyUSD(mSettingsAccess.getSavingsGoalValue()));
    }

    @Override
    public void update() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        double averageLunchCost = mSettingsAccess.getAverageLunchCost();
        int goal = mSettingsAccess.getSavingsGoalValue();
        int numLunchIns = mLunchInApi.getNumberOfLunchIns();
        double progress = numLunchIns * averageLunchCost;

        mGoalProgress.setText(getString(R.string.goal_progress,
                Formatter.formatDoubleToCurrencyUSD(progress),
                Formatter.formatIntToCurrencyUSD(goal)));
        mGoalRemaining.setText(getString(R.string.goal_remaining,
                Formatter.formatDoubleToCurrencyUSD(goal - progress),
                Formatter.formatIntToCurrencyUSD(goal)));
        mPieChartView.setPercentage(progress * 100 / goal);

        int numLunchesOut = mLunchInApi.getNumberOfLunchOuts();
        double moneySacrificed = numLunchesOut * averageLunchCost;
        mMoneySacrificed.setText(getString(R.string.money_sacrificed,
                Formatter.formatDoubleToCurrencyUSD(moneySacrificed)));
    }

    // TODO: Display number of hours you need to work to buy this lunch
    private void updateLunchOutUI() {
        mLunchInApi.setLunchOut();
        update();
        NotificationUtil.cancelNotification(getActivity());
        Toast.makeText(
                getActivity(),
                "Lunch out: " + mLunchInApi.getNumberOfLunchOuts() + "/" + mLunchInApi.getLunchTotal(),
                Toast.LENGTH_LONG).show();
    }

    private void updateLunchInUI() {
        mLunchInApi.setLunchIn();
        update();
        NotificationUtil.cancelNotification(getActivity());
        highlightGoalProgress();

        if (getProgressPercentage() >= 100) {
            showGoalReachedDialog();
        }
    }

    private double getProgressPercentage() {
        double averageLunchCost = mSettingsAccess.getAverageLunchCost();
        int goal = mSettingsAccess.getSavingsGoalValue();
        int numLunchIns = mLunchInApi.getNumberOfLunchIns();
        double progress = numLunchIns * averageLunchCost;
        return progress * 100 / goal;
    }

    private void highlightGoalProgress() {
        mGoalProgress.setBackgroundResource(R.color.highlight);
    }

    private void showGoalReachedDialog() {
        String goalName = mSettingsAccess.getSavingsGoalName();
        int goalCost = mSettingsAccess.getSavingsGoalValue();

        DialogFragment fragment = GoalReachedDialogFragment.newInstance(goalName, goalCost);
        fragment.show(getFragmentManager(), Constants.FRAGMENT_TAG_GOAL_REACHED_DIALOG);
    }
}