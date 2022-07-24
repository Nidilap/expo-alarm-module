package expo.modules.alarmemodule

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import android.content.Intent
import android.content.Context
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import androidx.core.os.bundleOf
import expo.modules.core.errors.ModuleDestroyedException
import expo.modules.core.utilities.ifNull
import expo.modules.kotlin.Promise
import expo.modules.kotlin.exception.CodedException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

class AlarmeModule : Module() {
  override fun definition() = ModuleDefinition {
    name("AlarmeModule")
    
    AsyncFunction("setAlarm") { details: ReadableMap, options: SetStringOptions ->
        val alarm: Alarm = parseAlarmObject(details)
        Manager.schedule(context, alarm)
        return@AsyncFunction true
    }
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