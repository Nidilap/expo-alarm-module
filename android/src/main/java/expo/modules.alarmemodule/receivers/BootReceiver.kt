package expo.modules.alarmemodule.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.alarm.Manager

class BootReceiver : BroadcastReceiver() {
    @Override
    fun onReceive(context: Context?, intent: Intent) {
        val action: String = intent.getAction()
        if (action != null && action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d(TAG, "received on boot intent: $action")
            Manager.reschedule(context)
        }
    }

    companion object {
        private const val TAG = "AlarmBootReceiver"
    }
}