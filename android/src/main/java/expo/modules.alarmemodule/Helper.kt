package com.alarm

import android.app.AlarmManager

internal object Helper {
    private const val TAG = "AlarmHelper"
    fun scheduleAlarm(context: Context, alarmUid: String?, triggerAtMillis: Long, notificationID: Int) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("ALARM_UID", alarmUid)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
        Log.d(TAG, "SDK version: " + Build.VERSION.SDK_INT)
        Log.d(TAG, "scheduling alarm with notification id: $notificationID")
        Log.d(TAG, "alarm scheduled to fire in " + (triggerAtMillis - System.currentTimeMillis()) as Float / (1000 * 60) + "min")
    }

    fun cancelAlarm(context: Context, notificationID: Int) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "canceling alarm with notification id: $notificationID")
    }

    fun sendNotification(context: Context, alarm: Alarm, notificationID: Int) {
        try {
            val mBuilder: Notification = getAlarmNotification(context, alarm, notificationID)
            val mNotificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(notificationID, mBuilder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAlarmNotification(context: Context, alarm: Alarm, notificationID: Int): Notification {
        return getNotification(context, notificationID, alarm.uid, alarm.title, alarm.description)
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
        manager.cancelAll()
    }

    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id: String = context.getResources().getString(R.string.notification_channel_id)
            val name: String = context.getResources().getString(R.string.notification_channel_name)
            val description: String = context.getResources().getString(R.string.notification_channel_desc)
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance)
            channel.setDescription(description)
            channel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            channel.setLightColor(Color.RED)
            channel.enableVibration(true)
            channel.setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager: NotificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "created a notification channel " + channel.toString())
        } else {
            Log.d(TAG, "didn't need to create a notification channel")
        }
    }

    internal fun getNotification(context: Context, id: Int, alarmUid: String, title: String?, description: String?): Notification {
        val res: Resources = context.getResources()
        val packageName: String = context.getPackageName()
        val smallIconResId: Int = res.getIdentifier("ic_launcher", "mipmap", packageName)
        val channelId: String = context.getResources().getString(R.string.notification_channel_id)
        val builder: NotificationCompat.Builder = Builder(context, channelId)
                .setSmallIcon(smallIconResId)
                .setContentTitle(title)
                .setTicker(null)
                .setContentText(description)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(false)
                .setSound(null)
                .setVibrate(null)
                .setContentIntent(createOnClickedIntent(context, alarmUid, id))
                .setDeleteIntent(createOnDismissedIntent(context, alarmUid, id))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val largeIconResId: Int = res.getIdentifier("ic_launcher", "mipmap", packageName)
            val largeIconBitmap: Bitmap = BitmapFactory.decodeResource(res, largeIconResId)
            if (largeIconResId != 0) builder.setLargeIcon(largeIconBitmap)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(NotificationCompat.CATEGORY_CALL)
            builder.setColor(Color.parseColor("blue"))
        }
        return builder.build()
    }

    private fun createOnClickedIntent(context: Context, alarmUid: String, notificationID: Int): PendingIntent {
        val resultIntent = Intent(context, getMainActivityClass(context))
        resultIntent.putExtra("ALARM_UID", alarmUid)
        return PendingIntent.getActivity(
                context,
                notificationID,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createOnDismissedIntent(context: Context, alarmUid: String, notificationId: Int): PendingIntent {
        val intent = Intent(context, DismissReceiver::class.java)
        intent.putExtra("NOTIFICATION_ID", notificationId)
        intent.putExtra("ALARM_UID", alarmUid)
        return PendingIntent.getBroadcast(context.getApplicationContext(), notificationId, intent, 0)
    }

    fun getDate(day: Int, hour: Int, minute: Int): Calendar {
        val date: Calendar = Calendar.getInstance()
        val today: Calendar = Calendar.getInstance()
        date.set(Calendar.DAY_OF_WEEK, day)
        date.set(Calendar.HOUR_OF_DAY, hour)
        date.set(Calendar.MINUTE, minute)
        date.set(Calendar.SECOND, 0)
        if (date.before(today)) {
            date.add(Calendar.DATE, 7)
        }
        return date
    }

    fun getMainActivityClass(context: Context): Class? {
        val packageName: String = context.getPackageName()
        val launchIntent: Intent = context.getPackageManager().getLaunchIntentForPackage(packageName)
        return try {
            val className: String = launchIntent.getComponent().getClassName()
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        } catch (e: NullPointerException) {
            e.printStackTrace()
            null
        }
    }
}