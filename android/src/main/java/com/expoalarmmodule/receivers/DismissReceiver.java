package com.expoalarmmodule.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.expoalarmmodule.Manager;

public class DismissReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmDismissReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve the alarm UID from the intent
        String alarmUid = intent.getStringExtra("ALARM_UID");

        // Stop the active alarm by invoking Manager.stop()
        Manager.stop(context);

        // Optionally log more information about the stop action
        Log.d(TAG, "Alarm " + alarmUid + " has been stopped.");
    }
}
