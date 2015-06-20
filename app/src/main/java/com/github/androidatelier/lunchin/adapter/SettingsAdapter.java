package com.github.androidatelier.lunchin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.activity.MainActivity;
import com.github.androidatelier.lunchin.model.Setting;
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
        settingViewHolder.icon.setImageResource(setting.getIcon());
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
            if (v.getContext()instanceof MainActivity) {
                final MainActivity activity = (MainActivity) v.getContext();
                final CharSequence titleText = title.getText();
                if (titleText.equals(Setting.TITLE_WIFI_WORK)) {
                    activity.doWifiScan();
                }

                else if (titleText.equals(Setting.TITLE_LUNCH_DAYS_TRACKED)) {
                    activity.displayDaysToTrackDialog();
                }

                else if (titleText.equals(Setting.TITLE_LUNCH_BEGIN)) {
                    // TODO: Use saved values
                    activity.displayTimePickerDialog(
                            Constants.DEFAULT_LUNCH_BEGIN_HOURS,
                            Constants.DEFAULT_LUNCH_BEGIN_MINUTES,
                            Constants.REQUEST_CODE_LUNCH_BEGIN_DIALOG);
                }

                else if (titleText.equals(Setting.TITLE_LUNCH_END)) {
                    // TODO: Use saved values
                    activity.displayTimePickerDialog(
                            Constants.DEFAULT_LUNCH_BEGIN_HOURS + Constants.DEFAULT_LUNCH_DURATION_HOURS,
                            Constants.DEFAULT_LUNCH_BEGIN_MINUTES,
                            Constants.REQUEST_CODE_LUNCH_END_DIALOG);
                }

                else if (titleText.equals(Setting.TITLE_LUNCH_AVG_COST)) {
                    activity.displayAverageLunchCostDialog();
                }

                else if (titleText.equals(Setting.TITLE_MY_GOAL)) {
                    activity.displayGoalSetterDialog();
                }
            }
        }
    }
}
