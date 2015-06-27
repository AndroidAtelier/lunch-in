package com.github.androidatelier.lunchin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.activity.MainActivity;
import com.github.androidatelier.lunchin.settings.Setting;
import com.github.androidatelier.lunchin.settings.SettingsAccess;
import com.github.androidatelier.lunchin.util.Constants;

import java.util.List;

/**
 * Created by brenda on 6/13/15.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingViewHolder> {
    List<Setting> mSettings;

    public SettingsAdapter(List<Setting> settings) {
        this.mSettings = settings;
    }

    @Override
    public SettingsAdapter.SettingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_setting, viewGroup, false);
        return new SettingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SettingsAdapter.SettingViewHolder settingViewHolder, int i) {
        Setting setting = mSettings.get(i);
        settingViewHolder.title.setText(setting.getTitle());
        settingViewHolder.description.setText(setting.getDescription());
    }

    @Override
    public int getItemCount() {
        return mSettings.size();
    }


    public final static class SettingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView description;
        private ImageView icon;

        SettingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // add cardview? maybe.
            title = (TextView)itemView.findViewById(R.id.row_setting_title);
            description = (TextView)itemView.findViewById(R.id.row_setting_description);
            icon = (ImageView)itemView.findViewById(R.id.row_setting_icon);

        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            if (context instanceof MainActivity) {
                final MainActivity activity = (MainActivity) context;
                final CharSequence titleText = title.getText();
                final SettingsAccess settingsAccess = new SettingsAccess(context);

                if (titleText.equals(Setting.Resource.WORK_WIFI.getTitleText(context))) {
                    activity.doWifiScan();
                }

                else if (titleText.equals(Setting.Resource.LUNCH_DAYS_TRACKED.getTitleText(context))) {
                    activity.displayDaysToTrackDialog();
                }

                else if (titleText.equals(Setting.Resource.LUNCH_START.getTitleText(context))) {
                    int[] startTime = settingsAccess.getLunchStartTime();
                    activity.displayTimePickerDialog(
                            startTime[0],
                            startTime[1],
                            Constants.REQUEST_CODE_LUNCH_START_DIALOG);
                }

                else if (titleText.equals(Setting.Resource.LUNCH_END.getTitleText(context))) {
                    int[] endTime = settingsAccess.getLunchEndTime();
                    activity.displayTimePickerDialog(
                            endTime[0],
                            endTime[1],
                            Constants.REQUEST_CODE_LUNCH_END_DIALOG);
                }

                else if (titleText.equals(Setting.Resource.LUNCH_AVG_COST.getTitleText(context))) {
                    activity.displayAverageLunchCostDialog(titleText, settingsAccess.getAverageLunchCost());
                }

                else if (titleText.equals(Setting.Resource.GROSS_SALARY.getTitleText(context))) {
                    activity.displayGrossSalaryDialog(titleText, settingsAccess.getGrossAnnualSalary(true));
                }

                else if (titleText.equals(Setting.Resource.MY_GOAL.getTitleText(context))) {
                    activity.displayGoalSetterDialog();
                }
            }
        }
    }
}
