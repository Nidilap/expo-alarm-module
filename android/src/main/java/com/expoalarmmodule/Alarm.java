package com.expoalarmmodule;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.expoalarmmodule.GsonUtil.createSerialize;



public class Alarm implements Cloneable {

    String uid;

    ZonedDateTime date;
    String dateString;

    ArrayList<Integer> days;
    int hour;
    int minutes;

    String title;
    String description;
    boolean repeating;
    boolean active;
    
    /**
     * Show dismiss button in notification. It is false by default.
     */
    boolean showDismiss;

    /**
     * Show snooze button. It is false by default.
     */
    boolean showSnooze;

    /**
     * Snooze interval in minutes
     * Default is 5 minutes
     */
    int snoozeInterval;

    /**
     * Custom texts for buttons of notifications
     */
    String dismissText;
    String snoozeText;

    Alarm(String uid, ArrayList<Integer> days, ZonedDateTime date, int hour, int minutes, boolean showDismiss, boolean showSnooze, int snoozeInterval, String title, String description, boolean repeating, boolean active, String dismissText, String snoozeText) {
        this.uid = uid;
        this.days = days;
        this.hour = hour;
        this.minutes = minutes;
        this.showDismiss = showDismiss;
        this.showSnooze = showSnooze;
        this.snoozeInterval = snoozeInterval;
        this.title = title;
        this.description = description;
        this.repeating = repeating;
        this.active = active;
        this.dismissText = dismissText;
        this.snoozeText = snoozeText;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    static Alarm fromJson(String json) {
      Alarm alarmTemp = createSerialize().fromJson(json, Alarm.class);
      alarmTemp.date = ZonedDateTime.parse(alarmTemp.dateString);
      return alarmTemp;
    }

    static String toJson(Alarm alarm) {
        alarm.dateString = alarm.date.toString();
        return createSerialize().toJson(alarm);
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
                this.showDismiss == alarm.showDismiss &&
                this.showSnooze == alarm.showSnooze &&
                this.snoozeInterval == alarm.snoozeInterval &&
                this.dismissText == alarm.dismissText &&
                this.snoozeText == alarm.snoozeText &&
                this.uid.equals(alarm.uid) &&
                this.days.equals(alarm.days) &&
                this.title.equals(alarm.title) &&
                this.description.equals(alarm.description)
        );
    }
}
