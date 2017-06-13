package com.bnsantos.dribble.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class Util {
  public static <T> T loadFromResource(@NonNull final ClassLoader loader, @NonNull final Gson gson, @NonNull final String fileName, @NonNull final Type type) throws IOException {
    InputStream inputStream = loader.getResourceAsStream("api-response/" + fileName);
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    JsonReader reader = new JsonReader(inputStreamReader);
    T data = gson.fromJson(reader, type);
    reader.close();
    return data;
  }
}
