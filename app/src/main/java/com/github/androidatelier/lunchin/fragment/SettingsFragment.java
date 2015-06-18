package com.github.androidatelier.lunchin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.SettingsAdapter;
import com.github.androidatelier.lunchin.model.Setting;

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



    private void initializeSettings() {
        mSettings = new ArrayList<Setting>();

        // app settings
        mSettings.add(new Setting(Setting.GROUP_APP_SETTINGS, Setting.TITLE_WIFI_WORK, "Select your work network", 0));

        // lunch settings
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_TIME, "Lunch start time", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_DURATION, "Length of lunch period", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_AVG_COST, "The average cost of lunch if you ate out", 0));

        // goal settings
        mSettings.add(new Setting(Setting.GROUP_GOAL_SETTINGS, Setting.TITLE_MY_GOAL, "Enter a savings goal", 0));
    }
}
