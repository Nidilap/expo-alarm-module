package com.expoalarmmodule.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.expoalarmmodule.AlarmService;
import com.expoalarmmodule.Manager;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmUid = intent.getStringExtra("ALARM_UID");
        Log.d(TAG, "received alarm: " + alarmUid);
        Toast.makeText(context, "received alarm: " + alarmUid, Toast.LENGTH_LONG).show();

        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("ALARM_UID", alarmUid);
        serviceIntent.putExtras(serviceIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
