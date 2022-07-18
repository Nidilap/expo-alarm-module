package com.alarm

import com.google.gson.Gson

class AlarmDates(alarmUid: String, dates: Array<Date>) {
    var uid: String
    var alarmUid: String
    var dates: Array<Date>
    var notificationIds: IntArray

    init {
        uid = alarmUid + postfix
        this.alarmUid = alarmUid
        this.dates = dates
        notificationIds = IntArray(dates.size)
        for (i in dates.indices) {
            notificationIds[i] = randomId()
        }
    }

    fun getNotificationId(date: Date?): Int {
        for (i in dates.indices) {
            if (dates[i].equals(date)) {
                return notificationIds[i]
            }
        }
        return -1
    }

    val currentNotificationId: Int
        get() {
            val current: Date? = currentDate
            return getNotificationId(current)
        }
    val currentDate: Date?
        get() {
            val calendar: Calendar = Calendar.getInstance()
            val currentDay: Int = calendar.get(Calendar.DAY_OF_WEEK)
            for (date in dates) {
                val cal: Calendar = Calendar.getInstance()
                cal.setTime(date)
                if (cal.get(Calendar.DAY_OF_WEEK) === currentDay) {
                    return date
                }
            }
            return null
        }

    fun getDates(): ArrayList<Date> {
        return ArrayList(Arrays.asList(dates))
    }

    fun update(old: Date?, updated: Date) {
        for (i in dates.indices) {
            if (dates[i].equals(old)) {
                dates[i] = updated
                return
            }
        }
    }

    companion object {
        private const val postfix = "_DATES"
        fun getDatesId(alarmUid: String?): String {
            return alarmUid + postfix
        }

        fun setNextWeek(date: Date?): Date {
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTime(date)
            calendar.add(Calendar.DATE, 7)
            return calendar.getTime()
        }

        fun snooze(date: Date?, minutes: Int): Date {
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTime(date)
            calendar.add(Calendar.MINUTE, minutes)
            return calendar.getTime()
        }

        fun isDatesId(id: String): Boolean {
            return id.contains(postfix)
        }

        fun fromJson(json: String?): AlarmDates {
            return Gson().fromJson(json, AlarmDates::class.java)
        }

        fun toJson(dates: AlarmDates?): String {
            return Gson().toJson(dates)
        }

        private fun randomId(): Int {
            return (Math.random() * 10000000)
        }
    }
}