package com.expoalarmmodule;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import io.goodforgod.gson.configuration.deserializer.LocalDateDeserializer;
import io.goodforgod.gson.configuration.deserializer.ZonedDateTimeDeserializer;
import io.goodforgod.gson.configuration.serializer.LocalDateSerializer;
import io.goodforgod.gson.configuration.serializer.ZonedDateTimeSerializer;

public class GsonUtil {
  // Constructor
  private GsonUtil() {
  }

  // General
  public static Gson create() {
    return new GsonBuilder().create();
  }

  public static Gson createSerialize() {
    // UTC format for date and time
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE))
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("UTC"))))
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("UTC"))))
        .create();
    } else {
      return new GsonBuilder().create();
    }
  }
}
