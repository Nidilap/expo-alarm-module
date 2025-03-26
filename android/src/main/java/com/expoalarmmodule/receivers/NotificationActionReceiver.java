package com.expoalarmmodule.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.expoalarmmodule.Manager;

public class NotificationActionReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationActionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "Intent or Action is null");
            return;
        }

        String action = intent.getAction();
        String alarmUid = intent.getStringExtra("ALARM_UID");

        if (alarmUid == null) {
            Log.e(TAG, "Alarm UID is missing!");
            return;
        }

        switch (action) {
            case "ACTION_STOP":
                Log.d(TAG, "Received STOP action for Alarm: " + alarmUid);
                Manager.stop(context);
                break;

            case "ACTION_SNOOZE":
                Log.d(TAG, "Received SNOOZE action for Alarm: " + alarmUid);
                Manager.snooze(context);
                break;

            default:
                Log.e(TAG, "Unknown action received: " + action);
                break;
        }
    }
}
