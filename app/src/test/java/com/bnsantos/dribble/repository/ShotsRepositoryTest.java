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
import com.bnsantos.dribble.vo.Resource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

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
    
    Mockito.when(mDao.read(anyInt(), anyInt())).thenReturn(Observable.<Resource<List<Shots>>>empty());
    Mockito.when(mService.read(anyInt(), anyInt(), anyString(), anyString())).thenReturn(Observable.just(data));
    Mockito.when(mDao.cache(data)).thenReturn(Observable.just(Resource.success(data)));

    RecordingObserver<Resource<List<Shots>>> subscriber = mSubscriberRule.create();

    mRepo.read(10).subscribe(subscriber);
    subscriber.assertValue(Resource.success(data)).assertComplete();
  }

  @Test
  public void testCacheAndNewResultsApi() throws IOException {
    List<Shots> cache = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots.json", new TypeToken<List<Shots>>() { }.getType());
    List<Shots> api = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots2.json", new TypeToken<List<Shots>>() { }.getType());

    Resource<List<Shots>> loading = Resource.loading(cache);
    Resource<List<Shots>> success = Resource.success(api);

    Mockito.when(mDao.read(anyInt(), anyInt())).thenReturn(Observable.just(loading));
    Mockito.when(mService.read(anyInt(), anyInt(), anyString(), anyString())).thenReturn(Observable.just(api));
    Mockito.when(mDao.cache(api)).thenReturn(Observable.just(success));

    RecordingObserver<Resource<List<Shots>>> subscriber = mSubscriberRule.create();

    mRepo.read(10).subscribe(subscriber);

    subscriber.assertValue(loading);
    subscriber.assertValue(success).assertComplete();
  }

  @Test
  public void testReadMore() throws IOException {
    List<Shots> page1 = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots_page1_limit10.json", new TypeToken<List<Shots>>() { }.getType());
    Resource<List<Shots>> p1Loading = Resource.loading(page1);
    Resource<List<Shots>> p1Success = Resource.success(page1);

    List<Shots> page2 = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots_page2_limit10.json", new TypeToken<List<Shots>>() { }.getType());
    Resource<List<Shots>> p2Loading = Resource.loading(page2);
    Resource<List<Shots>> p2Success = Resource.success(page2);

    List<Shots> page3 = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots_page3_limit10.json", new TypeToken<List<Shots>>() { }.getType());
    Resource<List<Shots>> p3Loading = Resource.loading(page3);
    Resource<List<Shots>> p3Success = Resource.success(page3);

    Mockito.when(mDao.read(0, 10)).thenReturn(Observable.just(p1Loading));
    Mockito.when(mService.read(eq(1), eq(10), anyString(), anyString())).thenReturn(Observable.just(page1));
    Mockito.when(mDao.cache(page1)).thenReturn(Observable.just(p1Success));

    RecordingObserver<Resource<List<Shots>>> readSubscriber = mSubscriberRule.create();
    mRepo.read(10).subscribe(readSubscriber);
    readSubscriber.assertValue(p1Loading);
    readSubscriber.assertValue(p1Success).assertComplete();

    Mockito.when(mDao.read(10, 10)).thenReturn(Observable.just(p2Loading));
    Mockito.when(mService.read(eq(2), eq(10), anyString(), anyString())).thenReturn(Observable.just(page2));
    Mockito.when(mDao.cache(page2)).thenReturn(Observable.just(p2Success));

    RecordingObserver<Resource<List<Shots>>> readPage1Subscriber = mSubscriberRule.create();
    mRepo.readPage(1, 10).subscribe(readPage1Subscriber);
    readPage1Subscriber.assertValue(p2Loading);
    readPage1Subscriber.assertValue(p2Success).assertComplete();

    Mockito.when(mDao.read(20, 10)).thenReturn(Observable.just(p3Loading));
    Mockito.when(mService.read(eq(3), eq(10), anyString(), anyString())).thenReturn(Observable.just(page3));
    Mockito.when(mDao.cache(page3)).thenReturn(Observable.just(p3Success));

    RecordingObserver<Resource<List<Shots>>> readPage2Subscriber = mSubscriberRule.create();
    mRepo.readPage(2, 10).subscribe(readPage2Subscriber);
    readPage2Subscriber.assertValue(p3Loading);
    readPage2Subscriber.assertValue(p3Success).assertComplete();
  }
}
