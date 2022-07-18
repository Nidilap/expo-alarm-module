package com.alarm

import android.content.Context

internal object Storage {
    fun saveAlarm(context: Context, alarm: Alarm) {
        val editor: SharedPreferences.Editor = getEditor(context)
        editor.putString(alarm.uid, Alarm.toJson(alarm))
        editor.apply()
    }

    fun saveDates(context: Context, dates: AlarmDates) {
        val editor: SharedPreferences.Editor = getEditor(context)
        editor.putString(dates.uid, AlarmDates.toJson(dates))
        editor.apply()
    }

    fun getAllAlarms(context: Context): Array<Alarm> {
        val alarms: ArrayList<Alarm> = ArrayList()
        val preferences: SharedPreferences = getSharedPreferences(context)
        val keyMap: Map<String, *> = preferences.getAll()
        for (entry in keyMap.entrySet()) {
            if (AlarmDates.isDatesId(entry.getKey())) continue
            alarms.add(Alarm.fromJson(entry.getValue() as String))
        }
        return alarms.toArray(arrayOfNulls<Alarm>(0))
    }

    fun getAlarm(context: Context, alarmUid: String?): Alarm {
        val preferences: SharedPreferences = getSharedPreferences(context)
        return Alarm.fromJson(preferences.getString(alarmUid, null))
    }

    fun getDates(context: Context, alarmUid: String?): AlarmDates {
        val preferences: SharedPreferences = getSharedPreferences(context)
        val json: String = preferences.getString(AlarmDates.getDatesId(alarmUid), null)
        return AlarmDates.fromJson(json)
    }

    fun removeAlarm(context: Context, alarmUid: String) {
        remove(context, alarmUid)
    }

    fun removeDates(context: Context, alarmUid: String?) {
        remove(context, AlarmDates.getDatesId(alarmUid))
    }

    private fun remove(context: Context, id: String) {
        val preferences: SharedPreferences = getSharedPreferences(context)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.remove(id)
        editor.apply()
    }

    private fun getEditor(context: Context): SharedPreferences.Editor {
        val sharedPreferences: SharedPreferences = getSharedPreferences(context)
        return sharedPreferences.edit()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        val fileKey: String = context.getResources().getString(R.string.notification_channel_id)
        return context.getSharedPreferences(fileKey, Context.MODE_PRIVATE)
    }
}