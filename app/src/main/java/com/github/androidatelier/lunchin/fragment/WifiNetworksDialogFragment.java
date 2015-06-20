package com.github.androidatelier.lunchin.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.github.androidatelier.lunchin.R;

import java.util.ArrayList;

public class WifiNetworksDialogFragment extends DialogFragment {
  private static final String KEY_NETWORKS = "networks";

  public static WifiNetworksDialogFragment newInstance(ArrayList<String> networks) {
    WifiNetworksDialogFragment fragment = new WifiNetworksDialogFragment();
    Bundle args = new Bundle();
    args.putStringArrayList(KEY_NETWORKS, networks);
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    ArrayList<String> networks = getArguments().getStringArrayList(KEY_NETWORKS);
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
          Toast.makeText(getActivity(), items[position], Toast.LENGTH_SHORT).show();
        }
      });
    }

    return builder.create();
  }
}