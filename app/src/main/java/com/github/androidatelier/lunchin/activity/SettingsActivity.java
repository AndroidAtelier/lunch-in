package com.github.androidatelier.lunchin.activity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.SettingsAdapter;
import com.github.androidatelier.lunchin.model.Setting;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private RecyclerView mRecyclerView;
    private List<Setting> mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mRecyclerView = (RecyclerView)findViewById(R.id.activity_settings_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        initializeSettings();
        RecyclerView.Adapter adapter = new SettingsAdapter(mSettings);
        mRecyclerView.setAdapter(adapter);

    }

    private void initializeSettings() {
        mSettings = new ArrayList<>();

        mSettings.add(new Setting(Setting.GROUP_APP_SETTINGS, Setting.TITLE_WIFI_WORK, "", 0));
        mSettings.add(new Setting(Setting.GROUP_APP_SETTINGS, Setting.TITLE_WIFI_HOME, "", 0));
        mSettings.add(new Setting(Setting.GROUP_APP_SETTINGS, Setting.TITLE_NOTIFICATIONS, "", 0));

        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_TIME, "Beginning and ending lunch times", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_DINNER_TIME, "Beginning and ending dinner times", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_LUNCH_AVG_COST, "The average cost of lunch if you ate out", 0));
        mSettings.add(new Setting(Setting.GROUP_USER_PREFERENCES, Setting.TITLE_DINNER_AVG_COST, "The average cost of dinner if you ate out", 0));

        mSettings.add(new Setting(Setting.GROUP_GOAL_SETTINGS, Setting.TITLE_MY_GOAL, "Select a savings goal", 0));
        //mSettings.add(new Setting(Setting.SETTING_GROUP_GOAL_SETTINGS, "Annual income", "Optional - So that we can calculate some cool stats for you, your info is kept private", 0));
        // do we still want this ^^ ?
    }

    public void doWifiScan() {
        mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        mWifiManager.startScan();

        List<ScanResult> availableNetworks = mWifiManager.getScanResults();
        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < availableNetworks.size(); i++) {
            String ssid = availableNetworks.get(i).SSID;
            // omit any networks not broadcasting ssid
            if (!TextUtils.isEmpty(ssid)) {
                resultList.add(ssid);
            }
        }

        displayWifiNetworksDialog(resultList);
    }

    private void displayWifiNetworksDialog(List<String> networks) {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setTitle("Select your network");
        dialog.setContentView(R.layout.dialog_wifi_list);
        ListView lv = (ListView)dialog.findViewById(R.id.dialog_wifi_list_listview);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, networks);
        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ssid = parent.getItemAtPosition(position).toString();
                Toast.makeText(dialog.getContext(), ssid, Toast.LENGTH_SHORT).show();
                //@todo persist this to ?? Database? SharedPreferences?
            }
        });
        dialog.show();
    }

}
