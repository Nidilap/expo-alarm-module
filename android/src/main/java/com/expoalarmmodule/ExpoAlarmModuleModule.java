package com.expoalarmmodule;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

@ReactModule(name = ExpoAlarmModuleModule.NAME)
public class ExpoAlarmModuleModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    public static final String NAME = "ExpoAlarmModule";

    public ExpoAlarmModuleModule(ReactApplicationContext reactContext) {
      super(reactContext);
      this.reactContext = reactContext;
      Helper.createNotificationChannel(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }



  @ReactMethod
  public void getState (Promise promise) {
    promise.resolve(Manager.getActiveAlarm());
  }

  @ReactMethod
  public void set (ReadableMap details, Promise promise) {

    Alarm alarm = parseAlarmObject(details);
    Manager.schedule(reactContext, alarm);
    promise.resolve(null);
  }

  @ReactMethod
  public void update (ReadableMap details, Promise promise) {
    Alarm alarm = parseAlarmObject(details);
    Manager.update(reactContext, alarm);
    promise.resolve(null);
  }

  @ReactMethod
  public void remove (String alarmUid, Promise promise) {
    Manager.remove(reactContext, alarmUid);
    promise.resolve(null);
  }

  @ReactMethod
  public void removeAll (Promise promise) {
    Manager.removeAll(reactContext);
    promise.resolve(null);
  }

  @ReactMethod
  public void enable (String alarmUid, Promise promise) {
    Manager.enable(reactContext, alarmUid);
    promise.resolve(null);
  }

  @ReactMethod
  public void disable (String alarmUid, Promise promise) {
    Manager.disable(reactContext, alarmUid);
    promise.resolve(null);
  }

  @ReactMethod
  public void stop (Promise promise) {
    Manager.stop(reactContext);
    Intent serviceIntent = new Intent(reactContext, AlarmService.class);
    reactContext.stopService(serviceIntent);
    promise.resolve(null);
  }

  @ReactMethod
  public void snooze (Promise promise) {
    Manager.snooze(reactContext);
    Intent serviceIntent = new Intent(reactContext, AlarmService.class);
    reactContext.stopService(serviceIntent);
    promise.resolve(null);
  }

  @ReactMethod
  public void get (String alarmUid, Promise promise) {
    try {
      Alarm alarm = Storage.getAlarm(reactContext, alarmUid);
      promise.resolve(serializeAlarmObject(alarm));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      promise.reject(e.getMessage(), e);
    }
  }

  @ReactMethod
  public void getAll (Promise promise) {
    try {
      Alarm[] alarms = Storage.getAllAlarms(reactContext);
      WritableNativeArray serializedAlarms = serializeArray(alarms);
      promise.resolve(serializedAlarms);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      promise.reject(e.getMessage(), e);
    }
  }

  private Alarm parseAlarmObject (ReadableMap alarm) {
    String uid = alarm.getString("uid");
    String title = alarm.getString("title");
    String description = alarm.getString("description");

    int hour = alarm.hasKey("hour") ?
      alarm.getInt("hour") : -1;

    int minutes = alarm.hasKey("minutes") ? alarm.getInt("minutes") : -1;

    boolean repeating = alarm.getBoolean("repeating");
    boolean active = alarm.getBoolean("active");

    boolean showDismiss = alarm.hasKey("showDismiss") ? alarm.getBoolean("showDismiss") : false;

    /**
     * Snooze configurations.
     * If snooze is not set, it will be false by default.
     * SnoozeInterval is 5 minutes by default.
     */
    boolean showSnooze = alarm.hasKey("showSnooze") ? alarm.getBoolean("showSnooze") : false;
    int snoozeInterval = alarm.hasKey("snoozeInterval") ? alarm.getInt("snoozeInterval") : 5;

    /**
     * Custom texts
     */
    String dismissText = alarm.hasKey("dismissText") ? alarm.getString("dismissText") : "Dismiss";
    String snoozeText = alarm.hasKey("snoozeText") ? alarm.getString("snoozeText") : "Snooze";

    ArrayList<Integer> days = new ArrayList<>();

    ZonedDateTime date = null;

    if (!alarm.isNull("day")) {
      try {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
          if(alarm.hasKey("day")) {
            ZonedDateTime dateTemp = ZonedDateTime.parse(alarm.getString("day"));
            // Date sucessful.
            date = dateTemp;
          }
        }
      } catch(Exception e) { // Se não conseguir obter a string, tenta obter o array
        System.out.println(e);
        ReadableArray rawDays = alarm.getArray("day");
        for (int i = 0; i < rawDays.size(); i++) {
          days.add(rawDays.getInt(i) + 1);
        }
      }
    }
    return new Alarm(uid, days, date, hour, minutes, showDismiss, showSnooze, snoozeInterval, title, description, repeating, active, dismissText, snoozeText);
  }

  private WritableMap serializeAlarmObject (Alarm alarm) throws Exception {
    WritableNativeMap map = new WritableNativeMap();
    map.putString("uid", alarm.uid);
    map.putString("title", alarm.title);
    map.putString("description", alarm.description);
    map.putBoolean("repeating", alarm.repeating);
    map.putBoolean("active", alarm.active);
    map.putBoolean("showDismiss", alarm.showDismiss);

    // Snooze configs
    map.putBoolean("showSnooze", alarm.showSnooze);
    map.putInt("snoozeInterval", alarm.snoozeInterval);

    // Custom texts
    map.putString("dismissText", alarm.dismissText);
    map.putString("snoozeText", alarm.snoozeText);



    if( alarm.date != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        map.putString("day", alarm.date.toString());
        map.putInt("minutes", alarm.date.getMinute());
        map.putInt("hour", alarm.date.getHour());
      } else {
        throw new Exception("Versao de android muito baixa!");
      }
    } else if(alarm.days.size() > 0) {
      map.putInt("hour", alarm.hour);
      map.putInt("minutes", alarm.minutes);
      map.putArray("days", serializeArray(alarm.days));
    }
    return map;
  }

  private WritableNativeArray serializeArray (ArrayList<Integer> a) {
    WritableNativeArray array = new WritableNativeArray();
    for (int value : a) array.pushInt(value);
    return array;
  }

  private WritableNativeArray serializeArray (Alarm[] a) throws Exception {
    WritableNativeArray array = new WritableNativeArray();
    for (Alarm alarm : a) array.pushMap(serializeAlarmObject(alarm));
    return array;
  }

}
