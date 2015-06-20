package com.github.androidatelier.lunchin.activity;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.ViewPagerAdapter;
import com.github.androidatelier.lunchin.fragment.MyGoalFragment;
import com.github.androidatelier.lunchin.fragment.SettingsFragment;
import com.github.androidatelier.lunchin.fragment.StatsFragment;
import com.github.androidatelier.lunchin.notification.NotificationUtil;
import com.github.androidatelier.lunchin.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private TabLayout mTabLayout;
    ViewPager mViewPager;

    private SettingsFragment mSettingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        initViewPager();
        setupViewPager(mViewPager);
        setupTablayout();

        String action = getIntent().getAction();
        if (NotificationUtil.ACTION_LUNCH_OUT.equals(action)) {
            updateLunchOutUI();
        }
        if (NotificationUtil.ACTION_LUNCH_IN.equals(action)) {
            updateLunchInUI();
        }
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // I'll uncomment these when I get the graphic sizing right for the lunch-in text icon I want to use
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setLogo(R.drawable.banner_lunchin);
        //toolbar.setLogoDescription("lunch-in toolbar title");
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
    }

    private void setupTablayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    // TODO: Display number of hours you need to work to buy this lunch
    private void updateLunchOutUI() {
        NotificationUtil.cancelNotification(this);
        Toast.makeText(this, "Lunch out", Toast.LENGTH_LONG).show();
    }

    // TODO: Update progress
    private void updateLunchInUI() {
        NotificationUtil.cancelNotification(this);
        Toast.makeText(this, "Lunch in", Toast.LENGTH_LONG).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MyGoalFragment(), "My Goal");
        adapter.addFrag(new StatsFragment(), "Statistics");
        adapter.addFrag(mSettingsFragment, "Settings");
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
        HashSet<String> results = new HashSet<>();

        for (int i = 0; i < availableNetworks.size(); i++) {
            String ssid = availableNetworks.get(i).SSID;
            // omit any networks not broadcasting ssid
            if (!TextUtils.isEmpty(ssid)) {
                results.add(ssid);
            }
        }
        ArrayList<String> resultList = new ArrayList<>(results);
        Collections.sort(resultList);

        mSettingsFragment.displayWifiNetworksDialog(resultList);
    }

    public void displayTimePickerDialog(int hours, int minutes, int requestCode) {
        mSettingsFragment.displayTimePickerDialog(hours, minutes, requestCode);
    }

    public void displayAverageLunchCostDialog(CharSequence title, float cost) {
        mSettingsFragment.displayEditTextDialog(
                title,
                String.valueOf(cost),
                Constants.REQUEST_CODE_LUNCH_COST_DIALOG);
    }

    public void displayDaysToTrackDialog() {
        mSettingsFragment.displayDaysToTrackDialog();
    }

    public void displayGoalSetterDialog() {
        mSettingsFragment.displayGoalSetterDialog();
    }

    private void setCheckedTextViewOnClickListener(final CheckedTextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.isChecked()) {
                    view.setChecked(false);
                } else {
                    view.setChecked(true);
                }

            }
        });
    }

    private void setPositiveButtonOnClickListener(final Button positiveButtonView, final Runnable onClickAction) {
        positiveButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @todo persist selection
            }
        });
    }

    private void setNeutralButtonOnClickListener(final Button neutralButtonView, final Dialog dialog) {
        neutralButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}