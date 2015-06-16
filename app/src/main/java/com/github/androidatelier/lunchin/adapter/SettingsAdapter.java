package com.github.androidatelier.lunchin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.activity.SettingsActivity;
import com.github.androidatelier.lunchin.model.Setting;

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
            if (v.getContext()instanceof SettingsActivity) {
                final SettingsActivity activity = (SettingsActivity) v.getContext();
                final CharSequence titleText = title.getText();
                if (titleText.equals(Setting.TITLE_WIFI_WORK) || titleText.equals(Setting.TITLE_WIFI_HOME)) {
                    activity.doWifiScan();
                }

                else if (titleText.equals(Setting.TITLE_LUNCH_TIME) || titleText.equals(Setting.TITLE_DINNER_TIME)) {
                    activity.displayTimePickerDialog(title.getText().toString());
                }

                else if (titleText.equals(Setting.TITLE_LUNCH_DURATION) || titleText.equals(Setting.TITLE_DINNER_DURATION)) {
                    activity.displayNumberPickerDialog(titleText.toString());
                }
            }

            Toast.makeText(v.getContext(), title.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
