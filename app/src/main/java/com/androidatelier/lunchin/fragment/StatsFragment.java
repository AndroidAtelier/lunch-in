package com.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidatelier.lunchin.R;
import com.androidatelier.lunchin.database.LunchInApi;
import com.androidatelier.lunchin.settings.SettingsAccess;
import com.androidatelier.lunchin.widget.StatsRow;

import java.text.NumberFormat;

/**
 * Created by brenda on 6/17/15.
 */
public class StatsFragment extends Fragment implements Updateable {
    private SettingsAccess mSettingsAccess;
    private LunchInApi mLunchInApi;

    private TextView mLunchCostInMinutes;
    private StatsRow mLunchInsThisMonth;
    private StatsRow mLunchOutsThisMonth;
    private StatsRow mLunchInsThisYear;
    private StatsRow mLunchOutsThisYear;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_stats,container,false);
        mLunchCostInMinutes = (TextView) v.findViewById(R.id.fragment_stats_tv2);
        mLunchInsThisMonth = (StatsRow) v.findViewById(R.id.lunch_ins_this_month);
        mLunchOutsThisMonth = (StatsRow) v.findViewById(R.id.lunch_outs_this_month);
        mLunchInsThisYear = (StatsRow) v.findViewById(R.id.lunch_ins_this_year);
        mLunchOutsThisYear = (StatsRow) v.findViewById(R.id.lunch_outs_this_year);

        mSettingsAccess = new SettingsAccess(getActivity());
        mLunchInApi = new LunchInApi(getActivity());

        update();

        return v;
    }

    @Override
    public void update() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        double salary = mSettingsAccess.getGrossAnnualSalary(true);
        double lunch_cost = mSettingsAccess.getAverageLunchCost();
        double hourly_wage = salary * 0.75/(40.0 * 52.0); //40 hours in a week and 52 weeks in a year
        //0.75 assumes a 25% tax rate

        String strHoursLunch = String.format("You would have to work %.1f minutes " +
                        "to buy lunch that costs %s with an annual salary of %s",
                lunch_cost/hourly_wage * 60,
                NumberFormat.getCurrencyInstance().format(lunch_cost),
                NumberFormat.getCurrencyInstance().format(salary));

        mLunchCostInMinutes.setText(strHoursLunch);

        mLunchInsThisMonth.setNumber(mLunchInApi.getNumberOfLunchInsThisMonth(), R.plurals.day);
        mLunchInsThisMonth.setNumberBackground(R.drawable.stats_round_green);
        mLunchOutsThisMonth.setNumber(mLunchInApi.getNumberOfLunchOutsThisMonth(), R.plurals.day);
        mLunchOutsThisMonth.setNumberBackground(R.drawable.stats_round_red);

        mLunchInsThisYear.setNumber(mLunchInApi.getNumberOfLunchInsThisYear(), R.plurals.day);
        mLunchInsThisYear.setNumberBackground(R.drawable.stats_round_green);
        mLunchOutsThisYear.setNumber(mLunchInApi.getNumberOfLunchOutsThisYear(), R.plurals.day);
        mLunchOutsThisYear.setNumberBackground(R.drawable.stats_round_red);
    }
}