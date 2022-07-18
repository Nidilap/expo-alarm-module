package com.alarm

import com.google.gson.Gson

class Alarm internal constructor(var uid: String, days: ArrayList<Integer?>, hour: Int, minutes: Int, snoozeInterval: Int, title: String, description: String, repeating: Boolean, active: Boolean) : Cloneable {
    var days: ArrayList<Integer>
    var hour: Int
    var minutes: Int
    var snoozeInterval: Int
    var title: String
    var description: String
    var repeating: Boolean
    var active: Boolean

    init {
        this.days = days
        this.hour = hour
        this.minutes = minutes
        this.snoozeInterval = snoozeInterval
        this.title = title
        this.description = description
        this.repeating = repeating
        this.active = active
    }

    val dates: Array<Any?>
        get() {
            val dates: Array<Date?> = arrayOfNulls<Date>(days.size())
            for (i in dates.indices) {
                val date: Calendar = Helper.getDate(days.get(i), hour, minutes)
                dates[i] = date.getTime()
            }
            return dates
        }
    val alarmDates: AlarmDates
        get() = AlarmDates(uid, dates)

    @Throws(CloneNotSupportedException::class)
    fun clone(): Alarm {
        return super.clone() as Alarm
    }

    @Override
    override fun equals(o: Object): Boolean {
        if (o === this) return true
        if (o !is Alarm) return false
        val alarm = o as Alarm
        return hour == alarm.hour && minutes == alarm.minutes && snoozeInterval == alarm.snoozeInterval &&
                uid.equals(alarm.uid) &&
                days.equals(alarm.days) &&
                title.equals(alarm.title) &&
                description.equals(alarm.description)
    }

    companion object {
        fun fromJson(json: String?): Alarm {
            return Gson().fromJson(json, Alarm::class.java)
        }

        fun toJson(alarm: Alarm?): String {
            return Gson().toJson(alarm)
        }
    }
}