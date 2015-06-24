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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.SettingsAdapter;
import com.github.androidatelier.lunchin.model.Setting;
import com.github.androidatelier.lunchin.model.SettingsAccess;
import com.github.androidatelier.lunchin.util.Constants;
import com.github.androidatelier.lunchin.util.DaysOfTheWeek;
import com.github.androidatelier.lunchin.util.Formatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Setting> mSettings;
    private SettingsAccess mSettingsAccess;
    private Formatter mFormatter;

    private DaysOfTheWeek daysToTrack;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        View v =inflater.inflate(R.layout.fragment_settings,container,false);

        mSettingsAccess = new SettingsAccess(getActivity());
        mFormatter = new Formatter(getActivity());
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
        String goalName = mSettingsAccess.getSavingsGoalName();
        int goalCost = mSettingsAccess.getSavingsGoalValue();

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
                String titleWorkWifi = getActivity().getString(Setting.Resource.WORK_WIFI.getTitle());
                updateSetting(
                        titleWorkWifi, data.getStringExtra(Constants.KEY_NETWORK));
                break;
            case Constants.REQUEST_CODE_DAYS_TO_TRACK_DIALOG:
                daysToTrack.bitVector = data.getIntExtra(Constants.KEY_DAYS_TO_TRACK, 0);
                String titleDaysTracked = getActivity().getString(Setting.Resource.LUNCH_DAYS_TRACKED.getTitle());
                updateSetting(titleDaysTracked, daysToTrack.toString());
                break;
            case Constants.REQUEST_CODE_LUNCH_START_DIALOG:
            case Constants.REQUEST_CODE_LUNCH_END_DIALOG:
                // TODO: If lunch end is before lunch begin, reset it to one hour after lunch begin
                int hours = data.getIntExtra(
                        Constants.KEY_HOURS, Constants.DEFAULT_LUNCH_BEGIN_HOURS);
                int minutes = data.getIntExtra(
                        Constants.KEY_MINUTES, Constants.DEFAULT_LUNCH_BEGIN_MINUTES);
                String titleLunchBegin = getActivity().getString(Setting.Resource.LUNCH_START.getTitle());
                String titleLunchEnd = getActivity().getString(Setting.Resource.LUNCH_END.getTitle());
                String title = (requestCode == Constants.REQUEST_CODE_LUNCH_START_DIALOG) ?
                        titleLunchBegin : titleLunchEnd;
                updateSetting(title, mFormatter.formatTime(hours, minutes));
                break;
            case Constants.REQUEST_CODE_LUNCH_COST_DIALOG:
                String text = data.getStringExtra(Constants.KEY_TEXT);
                if (!TextUtils.isEmpty(text)) {
                    try {
                        String titleLunchCost = getActivity().getString(Setting.Resource.LUNCH_AVG_COST.getTitle());
                        double cost = Double.parseDouble(text);
                        updateSetting(titleLunchCost,
                                NumberFormat.getCurrencyInstance(Locale.US).format(cost));
                    } catch (NumberFormatException e) {

                    }
                }
                break;
            case Constants.REQUEST_CODE_GROSS_SALARY_DIALOG:
                String salaryText = data.getStringExtra(Constants.KEY_TEXT);
                if (!TextUtils.isEmpty(salaryText)) {
                    String salaryTitle = Setting.Resource.GROSS_SALARY.getTitleText(getActivity());
                    int value = Integer.parseInt(salaryText);
                    updateSetting(salaryTitle, Formatter.formatIntToCurrencyUSD(value));
                }
            case Constants.REQUEST_CODE_GOAL_SETTER_DIALOG:
                String goalName = data.getStringExtra(Constants.KEY_GOAL_NAME);
                int goalCost = data.getIntExtra(Constants.KEY_GOAL_COST, -1);
                String goalTitle = getActivity().getString(Setting.Resource.MY_GOAL.getTitle());
                updateSetting(goalTitle, mFormatter.formatGoal(goalName, goalCost));
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void initializeSettings() {
        mSettings = new ArrayList<>();

        // app settings
        mSettings.add(new Setting(getActivity(), Setting.Resource.WORK_WIFI));

        // lunch settings
        daysToTrack.bitVector = Constants.DEFAULT_WORK_WEEK;  // TODO: Read saved value
        mSettings.add(new Setting(getActivity(), Setting.Resource.LUNCH_DAYS_TRACKED));
        mSettings.add(new Setting(getActivity(), Setting.Resource.LUNCH_START));
        mSettings.add(new Setting(getActivity(), Setting.Resource.LUNCH_END));
        mSettings.add(new Setting(getActivity(), Setting.Resource.LUNCH_AVG_COST));
        mSettings.add(new Setting(getActivity(), Setting.Resource.GROSS_SALARY));
        mSettings.add(new Setting(getActivity(), Setting.Resource.MY_GOAL));
    }

    // TODO: Save to preferences in dialogs
    private void updateSetting(String title, String description) {
        for (int i = 0; i < mSettings.size(); ++i) {
            Setting setting = mSettings.get(i);
            if (title.equals(setting.getTitle())) {
                mRecyclerView.getAdapter().notifyItemChanged(i);
            }
        }
    }

}