Include this receiver in your app manifest:
    <receiver
        android:name="com.github.androidatelier.lunchin.notification.NotificationReceiver"
        android:exported="false"/>

To trigger a notification:

    NotificationUtil.showLunchOutNotification(context, actionActivityClass);

or
    NotificationUtil.showLunchInNotification(context, actionActivityClass);

The "Lunch out?" notification start the action activity when the user chooses "yes" from the
notification.

The "Stayed in?" notification starts the action activity when the user chooses either "yes" or "no"
from the notification.

The action activity then reads `getIntent().getAction()`. `ACTION_LUNCH_IN` means the user stayed in
i.e. lunch hour passed and they answered "yes" when we asked them if they stayed in.
`ACTION_LUNCH_OUT` means the user went out, either by answering "yes" from the "Lunch out?"
notification or answering "no" from the "Stayed in?" notification.
