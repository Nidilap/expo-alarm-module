package com.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.alarm.AlarmService
import com.alarm.Manager

class AlarmReceiver : BroadcastReceiver() {
    @Override
    fun onReceive(context: Context, intent: Intent) {
        val alarmUid: String = intent.getStringExtra("ALARM_UID")
        Log.d(TAG, "received alarm: $alarmUid")
        Toast.makeText(context, "received alarm: $alarmUid", Toast.LENGTH_LONG).show()
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra("ALARM_UID", alarmUid)
        serviceIntent.putExtras(serviceIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    companion object {
        private const val TAG = "AlarmReceiver"
    }
}