package com.expoalarmmodule.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.expoalarmmodule.Manager;
import com.expoalarmmodule.Helper;
import com.expoalarmmodule.AlarmService;

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
                this.removeNotification(context, notificationId);
                break;

            case "SNOOZE_ACTION":
                Log.d(TAG, "Received SNOOZE action for Alarm: " + alarmUid);
                Manager.snooze(context);
                this.removeNotification(context, notificationId);
                break;

            default:
                Log.e(TAG, "Unknown action received: " + action);
                break;
        }
    }

    private void removeNotification(Context context, int notificationId) {
        Intent serviceIntentSnooze = new Intent(context, AlarmService.class);
        context.stopService(serviceIntentSnooze);
        if (notificationId != -1) {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(notificationId);
        }
    }
}
