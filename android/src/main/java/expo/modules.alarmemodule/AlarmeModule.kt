package expo.modules.alarmemodule

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import android.content.Intent

class AlarmeModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("AlarmeModule")
  }

  AsyncFunction("set") { details: ReadableMap, options: SetStringOptions ->
      val alarm: Alarm = parseAlarmObject(details)
      Manager.schedule(context, alarm)
      return@AsyncFunction true
  }

  private val context
    get() = requireNotNull(appContext.reactContext) {
      "React Application Context is null"
    }
    

  
  private fun parseAlarmObject(alarm: ReadableMap): Alarm {
      val uid: String = alarm.getString("uid")
      val title: String = alarm.getString("title")
      val description: String = alarm.getString("description")
      val hour: Int = alarm.getInt("hour")
      val minutes: Int = alarm.getInt("minutes")
      val snoozeInterval: Int = alarm.getInt("snoozeInterval")
      val repeating: Boolean = alarm.getBoolean("repeating")
      val active: Boolean = alarm.getBoolean("active")
      val days: ArrayList<Integer> = ArrayList()
      if (!alarm.isNull("days")) {
          val rawDays: ReadableArray = alarm.getArray("days")
          for (i in 0 until rawDays.size()) {
              days.add(rawDays.getInt(i))
          }
      }
      return Alarm(uid, days, hour, minutes, snoozeInterval, title, description, repeating, active)
  }

  private fun serializeAlarmObject(alarm: Alarm): WritableMap {
      val map = WritableNativeMap()
      map.putString("uid", alarm.uid)
      map.putString("title", alarm.title)
      map.putString("description", alarm.description)
      map.putInt("hour", alarm.hour)
      map.putInt("minutes", alarm.minutes)
      map.putInt("snoozeInterval", alarm.snoozeInterval)
      map.putArray("days", serializeArray(alarm.days))
      map.putBoolean("repeating", alarm.repeating)
      map.putBoolean("active", alarm.active)
      return map
  }

  private fun serializeArray(a: ArrayList<Integer>): WritableNativeArray {
      val array = WritableNativeArray()
      for (value in a) array.pushInt(value)
      return array
  }

  private fun serializeArray(a: Array<Alarm>): WritableNativeArray {
      val array = WritableNativeArray()
      for (alarm in a) array.pushMap(serializeAlarmObject(alarm))
      return array
  }

}