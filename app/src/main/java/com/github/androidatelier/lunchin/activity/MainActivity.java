package com.github.androidatelier.lunchin.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.ViewPagerAdapter;
import com.github.androidatelier.lunchin.fragment.MyGoalFragment;
import com.github.androidatelier.lunchin.fragment.SettingsFragment;
import com.github.androidatelier.lunchin.fragment.StatsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private TabLayout mTabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        initViewPager();
        setupViewPager(mViewPager);
        setupTablayout();
    }



    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
    }

    private void setupTablayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MyGoalFragment(), "My Goal");
        adapter.addFrag(new StatsFragment(), "Statistics");
        adapter.addFrag(new SettingsFragment(), "Settings");
        viewPager.setAdapter(adapter);
    }


    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    // settings dialogs
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
        final AppCompatDialog dialog = new AppCompatDialog(this, R.style.GlobalDialog);
        dialog.setTitle("Select your network");
        dialog.setContentView(R.layout.dialog_wifi_list);
        ListView lv = (ListView)dialog.findViewById(R.id.dialog_wifi_list_listview);

        if (networks.isEmpty()) {
            networks.add("No networks detected");
        }

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

    public void displayTimePickerDialog(String title) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // @todo persist this to somewhere
            }
        };

        final TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, 12, 0, false);
        dialog.setTitle(title);
        dialog.show();
    }

    public void displayNumberPickerDialog(String title) {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_number_picker);
        dialog.setTitle(title);

        Button positive = (Button) dialog.findViewById(R.id.dialog_number_picker_btn_ok);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @todo persist selection
            }
        });

        Button neutral = (Button)dialog.findViewById(R.id.dialog_number_picker_btn_cancel);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        NumberPicker picker = (NumberPicker)dialog.findViewById(R.id.dialog_number_picker_minutes);
        picker.setMaxValue(120);
        picker.setMinValue(0);
        // set to display whatever value they set it to, otherwise default to 60 mins
        picker.setValue(60);


        dialog.show();
    }

    public void displayGoalSetterDialog(String title) {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_goal);
        dialog.setTitle(title);
        EditText goal_name = (EditText)dialog.findViewById(R.id.dialog_goal_name);

        goal_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 if (actionId == EditorInfo.IME_ACTION_SEND) {
                    return true;
                 }
                 return false;
            }
        });

        EditText goal_cost = (EditText)dialog.findViewById(R.id.dialog_goal_cost);

        goal_cost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    return true;
                }
                return false;
            }
        });

        dialog.show();
    }
}


