package expo.modules.alarmemodule.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DismissReceiver : BroadcastReceiver() {
    @Override
    fun onReceive(context: Context?, intent: Intent) {
        // TODO: snooze alarm and send event to js
        Log.d(TAG, "dismissed alarm notification for: " + intent.getStringExtra("ALARM_UID"))
    }

    companion object {
        private const val TAG = "AlarmDismissReceiver"
    }
}