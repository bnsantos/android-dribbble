package com.bnsantos.dribble.repository;

import com.bnsantos.dribble.api.DribbleService;
import com.bnsantos.dribble.api.deserializers.DateDeserializer;
import com.bnsantos.dribble.api.deserializers.ShotsDeserializer;
import com.bnsantos.dribble.api.deserializers.UserDeserializer;
import com.bnsantos.dribble.db.ShotsDao;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;
import com.bnsantos.dribble.utils.RecordingObserver;
import com.bnsantos.dribble.utils.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.assertj.core.condition.AnyOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(JUnit4.class)
public class ShotsRepositoryTest {

  @Rule public RecordingObserver.Rule mSubscriberRule = new RecordingObserver.Rule();
  private DribbleService mService;
  private ShotsDao mDao;
  private ShotsRepository mRepo;
  private Gson mGson;

  @Before
  public void init(){
    mService = Mockito.mock(DribbleService.class);
    mDao = Mockito.mock(ShotsDao.class);
    mRepo = new ShotsRepository(mService, mDao);
    mGson = new GsonBuilder()
        .registerTypeAdapter(Date.class, new DateDeserializer())
        .registerTypeAdapter(User.class, new UserDeserializer())
        .registerTypeAdapter(Shots.class, new ShotsDeserializer())
        .create();
  }

  @Test
  public void testEmptyCache() throws IOException {
    List<Shots> data = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots.json", new TypeToken<List<Shots>>() {
    }.getType());
    
    Mockito.when(mDao.read()).thenReturn(Observable.<List<Shots>>empty());
    Mockito.when(mService.read(anyInt(), anyInt(), anyString(), anyString())).thenReturn(Observable.just(data));
    Mockito.when(mDao.cache(data)).thenReturn(Observable.just(data));

    RecordingObserver<List<Shots>> subscriber = mSubscriberRule.create();

    mRepo.read().subscribe(subscriber);

    Mockito.verify(mDao).read();
    Mockito.verify(mService).read(anyInt(), anyInt(), anyString(), anyString());
    Mockito.verify(mDao).cache(data);
    Mockito.verifyNoMoreInteractions(mDao);
    Mockito.verifyNoMoreInteractions(mService);

    subscriber.assertValue(data).assertComplete();
  }

  @Test
  public void testCacheAndNewResultsApi() throws IOException {
    List<Shots> cache = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots.json", new TypeToken<List<Shots>>() {
    }.getType());
    List<Shots> api = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots2.json", new TypeToken<List<Shots>>() {
    }.getType());

    Mockito.when(mDao.read()).thenReturn(Observable.just(cache));
    Mockito.when(mService.read(anyInt(), anyInt(), anyString(), anyString())).thenReturn(Observable.just(api));
    Mockito.when(mDao.cache(api)).thenReturn(Observable.just(api));

    RecordingObserver<List<Shots>> subscriber = mSubscriberRule.create();

    mRepo.read().subscribe(subscriber);

    subscriber.assertValue(cache);
    Mockito.verify(mDao).read();
    Mockito.verify(mService).read(anyInt(), anyInt(), anyString(), anyString());
    Mockito.verify(mDao).cache(api);
    Mockito.verifyNoMoreInteractions(mDao);
    Mockito.verifyNoMoreInteractions(mService);

    subscriber.assertValue(api).assertComplete();
  }
}
