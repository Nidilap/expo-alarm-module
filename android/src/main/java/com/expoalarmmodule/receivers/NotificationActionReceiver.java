package com.expoalarmmodule.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.expoalarmmodule.Manager;
import com.expoalarmmodule.Helper;

public class NotificationActionReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmNotificationActionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "Intent or Action is null");
            return;
        }

        String action = intent.getAction();
        String alarmUid = intent.getStringExtra("ALARM_UID");
        int notificationId = intent.getIntExtra("NOTIFICATION_ID", -1);

        if (alarmUid == null) {
            Log.e(TAG, "Alarm UID is missing!");
            return;
        }

        switch (action) {
            case "DISMISS_ACTION":
                Log.d(TAG, "Received DISMISS action for Alarm: " + alarmUid);
                Manager.stop(context);
                if (notificationId != -1) {
                    Helper.cancelNotification(context, notificationId);
                }
                break;

            case "SNOOZE_ACTION":
                Log.d(TAG, "Received SNOOZE action for Alarm: " + alarmUid);
                Manager.snooze(context);
                if (notificationId != -1) {
                    Helper.cancelNotification(context, notificationId);
                }
                break;

            default:
                Log.e(TAG, "Unknown action received: " + action);
                break;
        }
    }
}
