package com.github.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.SettingsAdapter;
import com.github.androidatelier.lunchin.model.Setting;
import com.github.androidatelier.lunchin.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brenda on 6/17/15.
 */
public class SettingsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Setting> mSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_settings,container,false);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_WIFI_NETWORKS_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    updateSetting(
                        Setting.TITLE_WIFI_WORK, data.getStringExtra(Constants.KEY_NETWORK));
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void initializeSettings() {
        mSettings = new ArrayList<Setting>();

        // app settings
        mSettings.add(new Setting(Setting.GROUP_APP_SETTINGS, Setting.TITLE_WIFI_WORK, "Select your work network", 0));

        // lunch settings
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_DAYS_TRACKED, "Select the days you want to track", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_BEGIN, "Lunch start time", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_END, "Lunch end time", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_AVG_COST, "The average cost of lunch if you ate out", 0));

        // goal settings
        mSettings.add(new Setting(Setting.GROUP_GOAL_SETTINGS, Setting.TITLE_MY_GOAL, "Enter a savings goal", 0));
    }

    // TODO: Save to preferences
    private void updateSetting(String title, String description) {
        for (int i = 0; i < mSettings.size(); ++i) {
            Setting setting = mSettings.get(i);
            if (title.equals(setting.getTitle())) {
                setting.setDescription(description);
                mRecyclerView.getAdapter().notifyItemChanged(i);
            }
        }
    }
}