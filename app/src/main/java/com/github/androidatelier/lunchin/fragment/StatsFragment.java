package com.github.androidatelier.lunchin.fragment;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.model.SettingsAccess;

import java.text.NumberFormat;

/**
 * Created by brenda on 6/17/15.
 */
public class StatsFragment extends Fragment{

    private SettingsAccess mSettingsAccess;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_stats,container,false);
        TextView tv2 = (TextView) v.findViewById(R.id.fragment_stats_tv2);

        mSettingsAccess = new SettingsAccess(getActivity());
        double salary = mSettingsAccess.getGrossAnnualSalary();
        double lunch_cost = mSettingsAccess.getAverageLunchCost();
        double hourly_wage = salary/(40.0 * 52.0); //40 hours in a week and 52 weeks in a year

        String strHoursLunch = String.format("You would have to work %.1f minutes " +
                "to buy lunch that costs %s with an annual salary of %s",
                lunch_cost/hourly_wage * 60,
                NumberFormat.getCurrencyInstance().format(lunch_cost),
                NumberFormat.getCurrencyInstance().format(salary));

        tv2.setText(strHoursLunch);
        return v;
    }

}
