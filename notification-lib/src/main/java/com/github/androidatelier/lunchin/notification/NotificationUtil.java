package com.github.androidatelier.lunchin.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public abstract class NotificationUtil  {
  public static final int NOTIFICATION_ID_LUNCH_OUT = 10000;
  public static final int NOTIFICATION_ID_LUNCH_IN = 10001;

  public static final String KEY_NOTIFICATION_ID = "notificationId";

  public static final String ACTION_LUNCH_OUT
      = "com.github.androidatelier.lunchin.notification.ACTION_LUNCH_OUT";
  public static final String ACTION_LUNCH_IN
      = "com.github.androidatelier.lunchin.notification.ACTION_LUNCH_IN";
  public static final String ACTION_DISMISS
      = "com.github.androidatelier.lunchin.notification.ACTION_DISMISS";

  public static void showLunchOutNotification(
      Context context, Class<?> actionActivityClass) {
    Notification notification
        = getLunchNotification(
        context, actionActivityClass, ACTION_LUNCH_OUT, NOTIFICATION_ID_LUNCH_OUT)
        .setContentText(context.getString(R.string.notification_text_lunch_out))
        .setSmallIcon(R.drawable.ic_notification_lunch_out)
        .setSound(Uri.parse("android.resource://"
            + context.getPackageName() + "/"
            + R.raw.sad_trombone))
        .build();
    notify(context, NOTIFICATION_ID_LUNCH_OUT, notification);
  }

  public static void showLunchInNotification(
      Context context, Class<?> actionActivityClass) {
    Notification notification = getLunchNotification(
        context, actionActivityClass, ACTION_LUNCH_IN,  NOTIFICATION_ID_LUNCH_IN)
        .setContentText(context.getString(R.string.notification_text_lunch_in))
        .setSmallIcon(R.drawable.ic_notification_lunch_in)
        .setSound(Uri.parse("android.resource://"
            + context.getPackageName() + "/"
            + R.raw.cash_register))
        .build();
    notify(context, NOTIFICATION_ID_LUNCH_IN, notification);
  }

  public static void cancelNotification(Context context) {
    NotificationManager manager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.cancelAll();
  }

  private static Notification.Builder getLunchNotification(
      Context context, Class<?> actionActivityClass, String action, int notificationId) {
    // Answering "yes" starts the action activity
    Intent yesIntent = new Intent(context, actionActivityClass);
    yesIntent.setAction(action);
    PendingIntent yesPendingIntent = PendingIntent.getActivity(context, 0, yesIntent, 0);

    // By default, the no action dismisses the notification
    Intent noIntent = new Intent(context, com.github.androidatelier.lunchin.notification.NotificationReceiver.class);
    noIntent.setAction(ACTION_DISMISS);
    noIntent.putExtra(KEY_NOTIFICATION_ID, notificationId);
    PendingIntent noPendingIntent = PendingIntent.getBroadcast(context, 0, noIntent, 0);

    // If the notificationId is NOTIFICATION_ID_LUNCH_IN, we asked if the user stayed in for lunch
    // after lunch time is over. If they answer no, they went out for lunch, so we start the action
    // activity with ACTION_LUNCH_OUT.
    if (notificationId == NOTIFICATION_ID_LUNCH_IN) {
      noIntent = new Intent(context, actionActivityClass);
      noIntent.setAction(ACTION_LUNCH_OUT);
      noPendingIntent = PendingIntent.getActivity(context, 0, noIntent, 0);
    }

    return new Notification.Builder(context)
        .setContentTitle(context.getString(R.string.notification_title))
        .setDefaults(Notification.DEFAULT_LIGHTS)
        .addAction(
            R.drawable.ic_action_yes, context.getString(R.string.action_yes), yesPendingIntent)
        .addAction(R.drawable.ic_action_no, context.getString(R.string.action_no), noPendingIntent);
  }

  private static void notify(Context context, int notificationId, Notification notification) {
    NotificationManager manager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(notificationId, notification);
  }
}