package com.bnsantos.dribble.viewmodel;

import com.bnsantos.dribble.api.deserializers.DateDeserializer;
import com.bnsantos.dribble.api.deserializers.ShotsDeserializer;
import com.bnsantos.dribble.api.deserializers.UserDeserializer;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;
import com.bnsantos.dribble.repository.ShotsRepository;
import com.bnsantos.dribble.utils.RecordingObserver;
import com.bnsantos.dribble.utils.Util;
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

@RunWith(JUnit4.class)
public class ShotsViewModelTest {
  @Rule
  public RecordingObserver.Rule mSubscriberRule = new RecordingObserver.Rule();

  private ShotsViewModel mViewModel;
  private ShotsRepository mRepository;
  private Gson mGson;

  @Before
  public void init(){
    mRepository = Mockito.mock(ShotsRepository.class);
    mViewModel = new ShotsViewModel(mRepository);
    mGson = new GsonBuilder()
        .registerTypeAdapter(Date.class, new DateDeserializer())
        .registerTypeAdapter(User.class, new UserDeserializer())
        .registerTypeAdapter(Shots.class, new ShotsDeserializer())
        .create();
  }

  @Test
  public void testRead() throws IOException {
    List<Shots> cache = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots.json", new TypeToken<List<Shots>>() {
    }.getType());

    List<Shots> server = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots2.json", new TypeToken<List<Shots>>() {
    }.getType());
    Mockito.when(mRepository.read()).thenReturn(Observable.just(cache, server));

    RecordingObserver<List<Shots>> subscriber = mSubscriberRule.create();
    mViewModel.read().subscribe(subscriber);

    Mockito.verify(mRepository).read();
    subscriber.assertValue(cache);
    subscriber.assertValue(server);
    subscriber.assertComplete();
  }
}
