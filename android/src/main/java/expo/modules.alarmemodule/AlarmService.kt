package com.alarm

import android.app.Notification

class AlarmService : Service() {
    @Override
    fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "On bind " + intent.getExtras())
        return null
    }

    // called only the first time we start the service
    @Override
    fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Creating service")
    }

    @Override
    fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Stopping service")
    }

    // triggered every time we call startService() when we start our service
    @Override
    fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "On start command")
        val alarmUid: String = intent.getStringExtra("ALARM_UID")
        val alarm: Alarm = Storage.getAlarm(getApplicationContext(), alarmUid)
        val notification: Notification = Helper.getAlarmNotification(this, alarm, 1)
        Manager.start(getApplicationContext(), alarmUid)
        startForeground(1, notification)

        // service will be explicitly started and stopped
        return START_STICKY
    }

    companion object {
        private const val TAG = "AlarmService"
    }
}