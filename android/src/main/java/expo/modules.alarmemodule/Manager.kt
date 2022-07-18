package com.alarm

import android.content.Context

object Manager {
    private const val TAG = "AlarmManager"
    private var sound: Sound? = null
    var activeAlarm: String? = null
        private set

    fun schedule(context: Context, alarm: Alarm) {
        val dates: AlarmDates = alarm.getAlarmDates()
        for (date in dates.getDates()) {
            Helper.scheduleAlarm(context, alarm.uid, date.getTime(), dates.getNotificationId(date))
        }
        Storage.saveAlarm(context, alarm)
        Storage.saveDates(context, dates)
    }

    fun reschedule(context: Context) {
        val alarms: Array<Alarm> = Storage.getAllAlarms(context)
        for (alarm in alarms) {
            Storage.removeDates(context, alarm.uid)
            val dates: AlarmDates = alarm.getAlarmDates()
            Storage.saveDates(context, dates)
            for (date in dates.getDates()) {
                Helper.scheduleAlarm(context, alarm.uid, date.getTime(), dates.getNotificationId(date))
                Log.d(TAG, "rescheduling alarm: " + alarm.uid)
            }
        }
    }

    fun update(context: Context, alarm: Alarm) {
        val prevDates: AlarmDates = Storage.getDates(context, alarm.uid)
        val dates: AlarmDates = alarm.getAlarmDates()
        for (date in dates.getDates()) {
            Helper.scheduleAlarm(context, alarm.uid, date.getTime(), dates.getNotificationId(date))
        }
        Storage.saveAlarm(context, alarm)
        Storage.saveDates(context, dates)
        if (prevDates == null) return
        for (date in prevDates.getDates()) {
            Helper.cancelAlarm(context, dates.getNotificationId(date))
        }
    }

    fun removeAll(context: Context) {
        val alarms: Array<Alarm> = Storage.getAllAlarms(context)
        for (alarm in alarms) {
            remove(context, alarm.uid)
        }
    }

    fun remove(context: Context, alarmUid: String?) {
        if (sound != null) {
            sound.stop()
        }
        val alarm: Alarm = Storage.getAlarm(context, alarmUid)
        val dates: AlarmDates = Storage.getDates(context, alarm.uid)
        Storage.removeAlarm(context, alarm.uid)
        Storage.removeDates(context, alarm.uid)
        if (dates == null) return
        for (date in dates.getDates()) {
            val notificationID: Int = dates.getNotificationId(date)
            Helper.cancelAlarm(context, notificationID)
        }
    }

    fun enable(context: Context, alarmUid: String?) {
        val alarm: Alarm = Storage.getAlarm(context, alarmUid)
        if (!alarm.active) {
            alarm.active = true
            Storage.saveAlarm(context, alarm)
        } else {
            Log.d(TAG, "Alarm already active - exiting job")
            return
        }
        val dates: AlarmDates = alarm.getAlarmDates()
        Storage.saveDates(context, dates)
        for (date in dates.getDates()) {
            Helper.scheduleAlarm(context, alarmUid, date.getTime(), dates.getNotificationId(date))
        }
    }

    fun disable(context: Context, alarmUid: String?) {
        val alarm: Alarm = Storage.getAlarm(context, alarmUid)
        if (alarm.active) {
            alarm.active = false
            Storage.saveAlarm(context, alarm)
        } else {
            Log.d(TAG, "Alarm already inactive - exiting job")
            return
        }
        val dates: AlarmDates = Storage.getDates(context, alarmUid)
        for (date in dates.getDates()) {
            Helper.cancelAlarm(context, dates.getNotificationId(date))
        }
    }

    fun start(context: Context, alarmUid: String?) {
        activeAlarm = alarmUid
        sound = Sound(context)
        sound.play("default")
        Log.d(TAG, "Starting " + activeAlarm)
    }

    fun stop(context: Context) {
        Log.d(TAG, "Stopping " + activeAlarm)
        sound!!.stop()
        val alarm: Alarm = Storage.getAlarm(context, activeAlarm)
        val dates: AlarmDates = Storage.getDates(context, activeAlarm)
        if (alarm.repeating) {
            val current: Date = dates.getCurrentDate()
            val updated: Date = AlarmDates.setNextWeek(current)
            dates.update(current, updated)
            Storage.saveDates(context, dates)
            Helper.scheduleAlarm(context, dates.alarmUid, updated.getTime(), dates.getCurrentNotificationId())
        } else {
            alarm.active = false
            Storage.saveAlarm(context, alarm)
            Storage.removeDates(context, activeAlarm)
        }
        activeAlarm = null
    }

    fun snooze(context: Context) {
        Log.d(TAG, "Snoozing " + activeAlarm)
        sound!!.stop()
        val alarm: Alarm = Storage.getAlarm(context, activeAlarm)
        val dates: AlarmDates = Storage.getDates(context, activeAlarm)
        val updated: Date = AlarmDates.snooze(Date(), alarm.snoozeInterval)
        dates.update(dates.getCurrentDate(), updated)
        Storage.saveDates(context, dates)
        Helper.scheduleAlarm(context, dates.alarmUid, updated.getTime(), dates.getCurrentNotificationId())
        activeAlarm = null
    }
}