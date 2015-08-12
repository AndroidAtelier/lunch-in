package com.androidatelier.lunchin.activity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.androidatelier.lunchin.R;
import com.androidatelier.lunchin.adapter.ViewPagerAdapter;
import com.androidatelier.lunchin.fragment.MyGoalFragment;
import com.androidatelier.lunchin.fragment.SettingsFragment;
import com.androidatelier.lunchin.fragment.StatsFragment;
import com.androidatelier.lunchin.fragment.Updateable;
import com.androidatelier.lunchin.util.Constants;

import java.util.ArrayList;
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

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MyGoalFragment(), "My Goal");
        adapter.addFrag(new StatsFragment(), "Statistics");
        adapter.addFrag(mSettingsFragment, "Settings");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getItem(position);
                if (fragment instanceof Updateable) {
                    ((Updateable) fragment).update();
                }
            }

            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });
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
        mSettingsFragment.displayWifiNetworksDialog(resultList);
    }

    public void displayTimePickerDialog(int hours, int minutes, int requestCode) {
        mSettingsFragment.displayTimePickerDialog(hours, minutes, requestCode);
    }

    public void displayAverageLunchCostDialog(CharSequence title, double cost) {
        mSettingsFragment.displayEditTextDialog(
                title,
                String.valueOf(cost),
                Constants.REQUEST_CODE_LUNCH_COST_DIALOG);
    }

    public void displayGrossSalaryDialog(CharSequence title, int grossAnnualSalary) {
        mSettingsFragment.displayEditTextDialog(
                title,
                String.valueOf(grossAnnualSalary),
                Constants.REQUEST_CODE_GROSS_SALARY_DIALOG);
    }
    public void displayDaysToTrackDialog() {
        mSettingsFragment.displayDaysToTrackDialog();
    }

    public void displayGoalSetterDialog() {
        mSettingsFragment.displayGoalSetterDialog();
    }

}