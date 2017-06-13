package com.bnsantos.dribble.api.deserializers;

import com.bnsantos.dribble.models.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class UserDeserializer implements JsonDeserializer<User> {
  @Override
  public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (json != null) {
      final JsonObject jsonObject = json.getAsJsonObject();
      final JsonObject linksObject = jsonObject.getAsJsonObject("links");

      String location;
      if (jsonObject.has("location") && !jsonObject.get("location").isJsonNull()){
        location = jsonObject.get("location").getAsString();
      }else {
        location = null;
      }


      return new User(
          jsonObject.get("id").getAsLong(),
          jsonObject.get("name").getAsString(),
          jsonObject.get("avatar_url").getAsString(),
          jsonObject.get("bio").getAsString(),
          location,
          linksObject != null && linksObject.has("web") ? linksObject.get("web").getAsString() : null,
          linksObject != null && linksObject.has("twitter") ? linksObject.get("twitter").getAsString() : null,
          jsonObject.get("followers_count").getAsLong(),
          jsonObject.get("likes_received_count").getAsLong(),
          jsonObject.get("pro").getAsBoolean(),
          (Date) context.deserialize(jsonObject.get("created_at"), Date.class),
          (Date) context.deserialize(jsonObject.get("updated_at"), Date.class)
          );
    }else {
      return null;
    }
  }
}
