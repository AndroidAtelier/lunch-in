package com.github.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.SettingsAdapter;
import com.github.androidatelier.lunchin.model.Setting;
import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.DaysOfTheWeek;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brenda on 6/17/15.
 */
public class SettingsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Setting> mSettings;

    private DaysOfTheWeek daysToTrack;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_settings,container,false);

        daysToTrack = new DaysOfTheWeek(
                getResources().getStringArray(R.array.days_of_the_week));

        mRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_settings_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        initializeSettings();
        RecyclerView.Adapter adapter = new SettingsAdapter(mSettings);
        mRecyclerView.setAdapter(adapter);

        return v;
    }

    public void displayWifiNetworksDialog(ArrayList<String> networks) {
        DialogFragment fragment = WifiNetworksDialogFragment.newInstance(networks);
        fragment.setTargetFragment(this, Constants.REQUEST_CODE_WIFI_NETWORKS_DIALOG);
        fragment.show(getFragmentManager(), Constants.FRAGMENT_TAG_WIFI_NETWORKS_DIALOG);
    }

    public void displayTimePickerDialog(int hours, int minutes, int requestCode) {
        DialogFragment fragment = TimePickerDialogFragment.newInstance(hours, minutes);
        fragment.setTargetFragment(this, requestCode);
        fragment.show(getFragmentManager(), Constants.FRAGMENT_TAG_TIME_PICKER_DIALOG);
    }

    public void displayDaysToTrackDialog() {
        // TODO: Read saved days to track to pass to DaysToTrackDialogFragment
        DialogFragment fragment
                = DaysToTrackDialogFragment.newInstance(Constants.DEFAULT_WORK_WEEK);
        fragment.setTargetFragment(this, Constants.REQUEST_CODE_DAYS_TO_TRACK_DIALOG);
        fragment.show(getFragmentManager(), Constants.FRAGMENT_TAG_DAYS_TO_TRACK_DIALOG);
    }

    public void displayEditTextDialog(CharSequence title, String text, int requestCode) {
        DialogFragment fragment
                = EditTextDialogFragment.newInstance(title, text);
        fragment.setTargetFragment(this, requestCode);
        fragment.show(getFragmentManager(), Constants.FRAGMENT_TAG_EDIT_TEXT_DIALOG);
    }

    public void displayGoalSetterDialog() {
        // TODO: Read saved goal name and amount to pass to GoalSetterDialogFragment
        String goalName = getString(R.string.default_goal_name);
        int goalCost = getResources().getInteger(R.integer.default_goal_cost);

        DialogFragment fragment = GoalSetterDialogFragment.newInstance(goalName, goalCost);
        fragment.setTargetFragment(this, Constants.REQUEST_CODE_GOAL_SETTER_DIALOG);
        fragment.show(getFragmentManager(), Constants.FRAGMENT_TAG_GOAL_SETTER_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        switch (requestCode) {
            case Constants.REQUEST_CODE_WIFI_NETWORKS_DIALOG:
                updateSetting(
                        Setting.TITLE_WIFI_WORK, data.getStringExtra(Constants.KEY_NETWORK));
                break;
            case Constants.REQUEST_CODE_DAYS_TO_TRACK_DIALOG:
                daysToTrack.bitVector = data.getIntExtra(Constants.KEY_DAYS_TO_TRACK, 0);
                updateSetting(Setting.TITLE_LUNCH_DAYS_TRACKED, daysToTrack.toString());
                break;
            case Constants.REQUEST_CODE_LUNCH_BEGIN_DIALOG:
            case Constants.REQUEST_CODE_LUNCH_END_DIALOG:
                // TODO: If lunch end is before lunch begin, reset it to one hour after lunch begin
                int hours = data.getIntExtra(
                        Constants.KEY_HOURS, Constants.DEFAULT_LUNCH_BEGIN_HOURS);
                int minutes = data.getIntExtra(
                        Constants.KEY_MINUTES, Constants.DEFAULT_LUNCH_BEGIN_MINUTES);
                String title = (requestCode == Constants.REQUEST_CODE_LUNCH_BEGIN_DIALOG) ?
                        Setting.TITLE_LUNCH_BEGIN : Setting.TITLE_LUNCH_END;
                updateSetting(title, formatLunchHour(hours, minutes));
                break;
            case Constants.REQUEST_CODE_LUNCH_COST_DIALOG:
                String text = data.getStringExtra(Constants.KEY_TEXT);
                if (!TextUtils.isEmpty(text)) {
                    try {
                        float cost = Float.parseFloat(text);
                        updateSetting(Setting.TITLE_LUNCH_AVG_COST,
                                NumberFormat.getCurrencyInstance().format(cost));
                    } catch (NumberFormatException e) {

                    }
                }
                break;
            case Constants.REQUEST_CODE_GOAL_SETTER_DIALOG:
                String goalName = data.getStringExtra(Constants.KEY_GOAL_NAME);
                float goalCost = data.getFloatExtra(Constants.KEY_GOAL_COST, -1);
                updateSetting(Setting.TITLE_MY_GOAL, formatGoal(goalName, goalCost));
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void initializeSettings() {
        mSettings = new ArrayList<>();

        // app settings
        mSettings.add(new Setting(Setting.GROUP_APP_SETTINGS, Setting.TITLE_WIFI_WORK, "Select your work network", 0));

        // lunch settings
        daysToTrack.bitVector = Constants.DEFAULT_WORK_WEEK;  // TODO: Read saved value
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_DAYS_TRACKED, daysToTrack.toString(), 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_BEGIN,
                formatLunchHour(Constants.DEFAULT_LUNCH_BEGIN_HOURS, Constants.DEFAULT_LUNCH_BEGIN_MINUTES), 0));  // TODO: Read saved value
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_END,
                formatLunchHour(
                        Constants.DEFAULT_LUNCH_BEGIN_HOURS + Constants.DEFAULT_LUNCH_DURATION_HOURS,
                        Constants.DEFAULT_LUNCH_BEGIN_MINUTES),
                0));  // TODO: Read saved value
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES,
                Setting.TITLE_LUNCH_AVG_COST, NumberFormat.getCurrencyInstance().format(Constants.DEFAULT_LUNCH_COST), 0));  // TODO: Read saved value

        // goal settings
        mSettings.add(new Setting(Setting.GROUP_GOAL_SETTINGS, Setting.TITLE_MY_GOAL, formatGoal(), 0));
    }

    // TODO: Save to preferences in dialogs
    private void updateSetting(String title, String description) {
        for (int i = 0; i < mSettings.size(); ++i) {
            Setting setting = mSettings.get(i);
            if (title.equals(setting.getTitle())) {
                setting.setDescription(description);
                mRecyclerView.getAdapter().notifyItemChanged(i);
            }
        }
    }

    private String formatLunchHour(int hours, int minutes) {
        return DateFormat.is24HourFormat(getActivity()) ?
                String.format("%02d:%02d", hours, minutes) :
                String.format("%d:%02d", convertTo12Hour(hours), minutes);
    }

    private static int convertTo12Hour(int hour) {
        int result = hour % 12;
        return (result == 0) ? 12 : result;
    }

    private String formatGoal() {
        // TODO: Read saved goal name and cost
        String goalName = getString(R.string.default_goal_name);
        int goalCost = getResources().getInteger(R.integer.default_goal_cost);

        return formatGoal(goalName, goalCost);
    }

    private String formatGoal(String goalName, float goalCost) {
        String defaultGoalName = getString(R.string.default_goal_name);
        int defaultGoalCost = getResources().getInteger(R.integer.default_goal_cost);

        if (TextUtils.isEmpty(goalName)) {
            goalName = defaultGoalName;
        }
        if (goalCost <= 0) {
            goalCost = defaultGoalCost;
        }

        return goalName + ": " + NumberFormat.getCurrencyInstance().format(goalCost);
    }
}