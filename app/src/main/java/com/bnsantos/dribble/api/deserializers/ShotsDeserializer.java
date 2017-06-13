package com.bnsantos.dribble.api.deserializers;

import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class ShotsDeserializer implements JsonDeserializer<Shots> {
  @Override
  public Shots deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (json != null) {
      final JsonObject jsonObject = json.getAsJsonObject();
      final JsonObject imagesObject = jsonObject.getAsJsonObject("images");

      String description;
      if (jsonObject.has("description") && !jsonObject.get("description").isJsonNull()){
        description = jsonObject.get("description").getAsString();
      }else {
        description = null;
      }

      String image = null;
      if(imagesObject != null && !imagesObject.isJsonNull()){
        if (imagesObject.has("hidpi") && !imagesObject.get("hidpi").isJsonNull()) {
          image = imagesObject.get("hidpi").getAsString();
        }else if(imagesObject.has("normal") && !imagesObject.get("normal").isJsonNull()){
          image = imagesObject.get("normal").getAsString();
        }
      }

      return new Shots(
          jsonObject.get("id").getAsLong(),
          jsonObject.get("title").getAsString(),
          description,
          jsonObject.get("width").getAsInt(),
          jsonObject.get("height").getAsInt(),
          image,
          jsonObject.get("views_count").getAsLong(),
          jsonObject.get("likes_count").getAsLong(),
          (Date) context.deserialize(jsonObject.get("created_at"), Date.class),
          (Date) context.deserialize(jsonObject.get("updated_at"), Date.class),
          jsonObject.get("html_url").getAsString(),
          jsonObject.get("animated").getAsBoolean(),
          (User) context.deserialize(jsonObject.get("user"), User.class)
      );
    }else {
      return null;
    }
  }
}
