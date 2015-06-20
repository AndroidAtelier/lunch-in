package com.github.androidatelier.lunchin.activity;

import android.app.Dialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.adapter.ViewPagerAdapter;
import com.github.androidatelier.lunchin.fragment.MyGoalFragment;
import com.github.androidatelier.lunchin.fragment.SettingsFragment;
import com.github.androidatelier.lunchin.fragment.StatsFragment;
import com.github.androidatelier.lunchin.model.Setting;
import com.github.androidatelier.lunchin.notification.NotificationUtil;

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

    // TODO: Display number of hours you need to work to by this lunch
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

    public void displayTimePickerDialog(String title, final boolean isStartTime) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (isStartTime) {
                    // @todo persist lunch start time
                } else {
                    // @todo persist lunch end time
                }
            }
        };

        TimePickerDialog dialog = null;
        if (title.equals(Setting.TITLE_LUNCH_BEGIN)) {
            dialog = new TimePickerDialog(this, timeSetListener, 12, 0, false);
        } else {
            dialog = new TimePickerDialog(this, timeSetListener, 13, 0, false);
        }

        dialog.setTitle(title);
        dialog.show();
    }

    public void displayAverageLunchCostDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_lunch_cost);
        TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_lunch_cost_title);
        dialogTitle.setText("Average Lunch Cost");

        Button positive = (Button) dialog.findViewById(R.id.dialog_lunch_cost_btn_ok);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //@todo the logic to persist to database here
                dialog.dismiss();
            }
        };
        setPositiveButtonOnClickListener(positive, runnable);

        Button neutral = (Button)dialog.findViewById(R.id.dialog_lunch_cost_btn_cancel);
        setNeutralButtonOnClickListener(neutral, dialog);
        dialog.show();
    }

    public void displayDaysToTrackDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_days_to_track);
        TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_days_to_track_title);
        dialogTitle.setText("Days to Track");

        final CheckedTextView sat = (CheckedTextView)dialog.findViewById(R.id.dialog_days_to_track_checkedtv_sat);
        final CheckedTextView sun = (CheckedTextView)dialog.findViewById(R.id.dialog_days_to_track_checkedtv_sun);
        final CheckedTextView mon = (CheckedTextView)dialog.findViewById(R.id.dialog_days_to_track_checkedtv_mon);
        final CheckedTextView tue = (CheckedTextView)dialog.findViewById(R.id.dialog_days_to_track_checkedtv_tue);
        final CheckedTextView wed = (CheckedTextView)dialog.findViewById(R.id.dialog_days_to_track_checkedtv_wed);
        final CheckedTextView thu = (CheckedTextView)dialog.findViewById(R.id.dialog_days_to_track_checkedtv_thu);
        final CheckedTextView fri = (CheckedTextView)dialog.findViewById(R.id.dialog_days_to_track_checkedtv_fri);

        setCheckedTextViewOnClickListener(sat);
        setCheckedTextViewOnClickListener(sun);
        setCheckedTextViewOnClickListener(mon);
        setCheckedTextViewOnClickListener(tue);
        setCheckedTextViewOnClickListener(wed);
        setCheckedTextViewOnClickListener(thu);
        setCheckedTextViewOnClickListener(fri);

        dialog.show();
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