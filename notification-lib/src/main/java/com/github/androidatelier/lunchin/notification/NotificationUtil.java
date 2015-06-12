package com.github.androidatelier.lunchin.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public abstract class NotificationUtil  {
  public static final int NOTIFICATION_ID_LUNCH_OUT = 10000;
  public static final int NOTIFICATION_ID_LUNCH_IN = 10001;

  public static final String ACTION_LUNCH_OUT
      = "com.github.androidatelier.lunchin.notification.ACTION_LUNCH_OUT";
  public static final String ACTION_LUNCH_IN
      = "com.github.androidatelier.lunchin.notification.ACTION_LUNCH_IN";

  public static void showLunchOutNotification(
      Context context, Class<?> yesActivityClass) {
    Notification notification = getLunchNotification(context, yesActivityClass, ACTION_LUNCH_OUT)
        .setContentText(context.getString(R.string.notification_text_lunch_out))
        .setSmallIcon(R.drawable.ic_notification_lunch_out)
        .build();
    notify(context, NOTIFICATION_ID_LUNCH_OUT, notification);
  }

  public static void showLunchInNotification(
      Context context, Class<?> yesActivityClass) {
    Notification notification = getLunchNotification(context, yesActivityClass, ACTION_LUNCH_IN)
        .setContentText(context.getString(R.string.notification_text_lunch_in))
        .setSmallIcon(R.drawable.ic_notification_lunch_in)
        .build();
    notify(context, NOTIFICATION_ID_LUNCH_IN, notification);
  }

  public static void cancelNotification(Context context) {
    NotificationManager manager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.cancelAll();
  }

  private static Notification.Builder getLunchNotification(
      Context context, Class<?> yesActivityClass, String action) {
    Intent intent = new Intent(context, yesActivityClass);
    intent.setAction(action);
    PendingIntent yesIntent = PendingIntent.getActivity(context, 0, intent, 0);

    return new Notification.Builder(context)
        .setContentTitle(context.getString(R.string.notification_title))
        .addAction(
            R.drawable.ic_action_yes, context.getString(R.string.action_yes), yesIntent)
        .addAction(R.drawable.ic_action_no, context.getString(R.string.action_no), null);
  }

  private static void notify(Context context, int notificationId, Notification notification) {
    NotificationManager manager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(notificationId, notification);
  }
}