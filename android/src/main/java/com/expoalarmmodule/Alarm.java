package com.expoalarmmodule;

import android.os.Build;

import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Alarm implements Cloneable {

    String uid;

    ZonedDateTime date;

    ArrayList<Integer> days;
    int hour;
    int minutes;

    int snoozeInterval;
    String title;
    String description;
    boolean repeating;
    boolean active;

    Alarm(String uid, ArrayList<Integer> days, ZonedDateTime date, int hour, int minutes, int snoozeInterval, String title, String description, boolean repeating, boolean active) {
        this.uid = uid;
        this.days = days;
        this.hour = hour;
        this.minutes = minutes;
        this.snoozeInterval = snoozeInterval;
        this.title = title;
        this.description = description;
        this.repeating = repeating;
        this.active = active;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          this.date = date;
        }
    }

    List<Date> getDates() {
        List<Date> dates = new ArrayList<>();

        if(date != null) {
          Calendar triggerDate = null;
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            triggerDate = GregorianCalendar.from(date);
            dates.add(triggerDate.getTime());
          }
        } else if (days != null) {
          for (int i = 0; i < days.size(); i++) {
            Calendar date = Helper.getDate(days.get(i), hour, minutes);
            dates.add(date.getTime());
          }
        }
        return dates;
    }

    AlarmDates getAlarmDates() {
        return new AlarmDates(uid, getDates());
    }

    static Alarm fromJson(String json) {
        return new Gson().fromJson(json, Alarm.class);
    }

    static String toJson(Alarm alarm) {
        return new Gson().toJson(alarm);
    }

    public Alarm clone () throws CloneNotSupportedException {
        return (Alarm) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Alarm)) return false;
        Alarm alarm = (Alarm)o;
        return (
                this.hour == alarm.hour &&
                this.minutes == alarm.minutes &&
                this.snoozeInterval == alarm.snoozeInterval &&
                this.uid.equals(alarm.uid) &&
                this.days.equals(alarm.days) &&
                this.title.equals(alarm.title) &&
                this.description.equals(alarm.description)
        );
    }
}
