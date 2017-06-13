package com.bnsantos.dribble.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateDeserializer implements JsonDeserializer<Date> {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    try {
      return json == null ? null : df.parse(json.getAsString());
    } catch (ParseException e) {
      return null;
    }
  }
}
