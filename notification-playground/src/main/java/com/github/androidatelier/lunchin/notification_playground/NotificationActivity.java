package com.github.androidatelier.lunchin.notification_playground;

import android.app.Activity;
import android.os.Bundle;

import com.github.androidatelier.lunchin.notification.NotificationUtil;

public class NotificationActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String action = getIntent().getAction();
    if (NotificationUtil.ACTION_LUNCH_OUT.equals(action)) {
      lunchOut();
    }
    if (NotificationUtil.ACTION_LUNCH_IN.equals(action)) {
      lunchIn();
    }
  }

  public void lunchOut() {
    NotificationUtil.cancelNotification(this);
    getWindow().setBackgroundDrawableResource(android.R.color.holo_red_light);
    setTitle(R.string.lunch_out);
  }

  public void lunchIn() {
    NotificationUtil.cancelNotification(this);
    getWindow().setBackgroundDrawableResource(android.R.color.holo_green_light);
    setTitle(R.string.lunch_in);
  }
}