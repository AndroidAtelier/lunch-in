package com.github.androidatelier.lunchin.notification_playground;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.github.androidatelier.lunchin.notification.NotificationUtil;


public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void lunchOut(View v) {
    NotificationUtil.showLunchOutNotification(this, NotificationActivity.class);
  }

  public void lunchIn(View v) {
    NotificationUtil.showLunchInNotification(this, NotificationActivity.class);
  }
}