package com.bnsantos.dribble.di.modules;

import android.support.annotation.NonNull;

import com.bnsantos.dribble.BuildConfig;
import com.bnsantos.dribble.api.DribbleService;
import com.bnsantos.dribble.api.deserializers.DateDeserializer;
import com.bnsantos.dribble.api.deserializers.ShotsDeserializer;
import com.bnsantos.dribble.api.deserializers.UserDeserializer;
import com.bnsantos.dribble.db.ShotsDao;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;
import com.bnsantos.dribble.vo.Resource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@Module
public class TestModule extends AppModule{
  @Override
  Gson gson(){
    return new GsonBuilder()
        .registerTypeAdapter(Date.class, new DateDeserializer())
        .registerTypeAdapter(User.class, new UserDeserializer())
        .registerTypeAdapter(Shots.class, new ShotsDeserializer())
        .create();
  }

  @Singleton @Provides
  DribbleService dribbleService(@NonNull final Gson gson){
    DribbleService mock = Mockito.mock(DribbleService.class);
    try {
      List<Shots> page0 = loadFromResource(getClass().getClassLoader(), gson, "shots_page1_limit10.json", new TypeToken<List<Shots>>() {
      }.getType());

      List<Shots> page1 = loadFromResource(getClass().getClassLoader(), gson, "shots_page2_limit10.json", new TypeToken<List<Shots>>() {
      }.getType());

      List<Shots> page2 = loadFromResource(getClass().getClassLoader(), gson, "shots_page3_limit10.json", new TypeToken<List<Shots>>() {
      }.getType());

      Mockito.when(mock.read(eq(1), eq(BuildConfig.SHOTS_PAGE), anyString(), anyString())).thenReturn(Observable.just(page0));
      Mockito.when(mock.read(eq(2), eq(BuildConfig.SHOTS_PAGE), anyString(), anyString())).thenReturn(Observable.just(page1));
      Mockito.when(mock.read(eq(3), eq(BuildConfig.SHOTS_PAGE), anyString(), anyString())).thenReturn(Observable.just(page2));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return mock;
  }

  @Singleton @Provides
  ShotsDao shotsDao(){
    ShotsDao mock = Mockito.mock(ShotsDao.class);

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Date.class, new DateDeserializer())
        .registerTypeAdapter(User.class, new UserDeserializer())
        .registerTypeAdapter(Shots.class, new ShotsDeserializer())
        .create();

    try {
      List<Shots> page0 = loadFromResource(getClass().getClassLoader(), gson, "shots_page1_limit10.json", new TypeToken<List<Shots>>() {
      }.getType());

      List<Shots> page1 = loadFromResource(getClass().getClassLoader(), gson, "shots_page2_limit10.json", new TypeToken<List<Shots>>() {
      }.getType());

      List<Shots> page2 = loadFromResource(getClass().getClassLoader(), gson, "shots_page3_limit10.json", new TypeToken<List<Shots>>() {
      }.getType());

      Mockito.when(mock.read(0, BuildConfig.SHOTS_PAGE)).thenReturn(Observable.just(Resource.<List<Shots>>loading(new ArrayList<Shots>())));
      Mockito.when(mock.read(10, BuildConfig.SHOTS_PAGE)).thenReturn(Observable.just(Resource.<List<Shots>>loading(new ArrayList<Shots>())));
      Mockito.when(mock.read(20, BuildConfig.SHOTS_PAGE)).thenReturn(Observable.just(Resource.<List<Shots>>loading(new ArrayList<Shots>())));

      Mockito.when(mock.cache(page0)).thenReturn(Observable.just(Resource.success(page0)));
      Mockito.when(mock.cache(page1)).thenReturn(Observable.just(Resource.success(page1)));
      Mockito.when(mock.cache(page2)).thenReturn(Observable.just(Resource.success(page2)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return mock;
  }

  public static <T> T loadFromResource(@NonNull final ClassLoader loader, @NonNull final Gson gson, @NonNull final String fileName, @NonNull final Type type) throws IOException {
    InputStream inputStream = loader.getResourceAsStream("api-response/" + fileName);
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    JsonReader reader = new JsonReader(inputStreamReader);
    T data = gson.fromJson(reader, type);
    reader.close();
    return data;
  }
}
