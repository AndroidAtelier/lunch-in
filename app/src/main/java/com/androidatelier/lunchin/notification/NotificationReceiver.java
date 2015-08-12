package com.androidatelier.lunchin.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (NotificationUtil.ACTION_DISMISS.equals(intent.getAction())) {
      int notificationId = intent.getIntExtra(NotificationUtil.KEY_NOTIFICATION_ID, 0);
      NotificationManager manager
          = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      manager.cancel(notificationId);
    }
  }
}