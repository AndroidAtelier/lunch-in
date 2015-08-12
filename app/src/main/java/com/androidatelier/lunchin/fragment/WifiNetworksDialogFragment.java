package com.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.androidatelier.lunchin.R;
import com.androidatelier.lunchin.receiver.LunchInDetectionReceiver;
import com.androidatelier.lunchin.receiver.LunchOutDetectionReceiver;
import com.androidatelier.lunchin.settings.SettingsAccess;
import com.androidatelier.lunchin.util.Constants;
import com.androidatelier.lunchin.utils.LunchInDetector;

import java.util.ArrayList;

public class WifiNetworksDialogFragment extends DialogFragment {
  public static WifiNetworksDialogFragment newInstance(ArrayList<String> networks) {
    WifiNetworksDialogFragment fragment = new WifiNetworksDialogFragment();
    Bundle args = new Bundle();
    args.putStringArrayList(Constants.KEY_NETWORKS, networks);
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    ArrayList<String> networks = getArguments().getStringArrayList(Constants.KEY_NETWORKS);
    if (networks == null || networks.isEmpty()) {
      builder.setMessage(R.string.no_networks_detected);
    } else {
      builder.setTitle(R.string.dialog_title_wifi_networks);
      final CharSequence[] items = new CharSequence[networks.size()];
      for (int i = 0; i < networks.size(); ++i) {
        items[i] = networks.get(i);
      }
      builder.setItems(items, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int position) {
            SettingsAccess settingsAccess = new SettingsAccess(getActivity());
            String originalWorkWifiId = settingsAccess.getWorkWifiId();

            // Only turn on the receiver if its the user's first time setting it up
            if((originalWorkWifiId == null) || (originalWorkWifiId.isEmpty())){
                PackageManager pm  = getActivity().getPackageManager();
                ComponentName componentName = new ComponentName(getActivity(), LunchOutDetectionReceiver.class);
                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }

            // update shared prefs
            settingsAccess.setWorkWifiId(items[position].toString());

            // set lunch in alarm
            if (TextUtils.isEmpty(originalWorkWifiId)) {
                LunchInDetector.setAlarm(getActivity(), LunchInDetectionReceiver.class);
            }

            // update UI
            Intent data = new Intent();
            data.putExtra(Constants.KEY_NETWORK, items[position]);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        }
      });
    }

    return builder.create();
  }
}