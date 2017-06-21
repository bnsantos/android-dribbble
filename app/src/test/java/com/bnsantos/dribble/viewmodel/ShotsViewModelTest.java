package com.bnsantos.dribble.viewmodel;

import com.bnsantos.dribble.api.deserializers.DateDeserializer;
import com.bnsantos.dribble.api.deserializers.ShotsDeserializer;
import com.bnsantos.dribble.api.deserializers.UserDeserializer;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;
import com.bnsantos.dribble.repository.ShotsRepository;
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
import io.reactivex.exceptions.CompositeException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

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
    List<Shots> cache = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots.json", new TypeToken<List<Shots>>() {}.getType());
    List<Shots> server = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots2.json", new TypeToken<List<Shots>>() { }.getType());

    Resource<List<Shots>> loading = Resource.loading(cache);
    Resource<List<Shots>> success = Resource.success(server);
    Mockito.when(mRepository.read(anyInt())).thenReturn(Observable.just(loading, success));

    RecordingObserver<Resource<List<Shots>>> subscriber = mSubscriberRule.create();
    mViewModel.read().subscribe(subscriber);

    Mockito.verify(mRepository).read(10);
    subscriber.assertValue(loading);
    subscriber.assertValue(success);
    subscriber.assertComplete();
  }

  @Test
  public void testReadNext() throws IOException {
    List<Shots> page1 = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots_page1_limit10.json", new TypeToken<List<Shots>>() { }.getType());
    Resource<List<Shots>> p1Loading = Resource.loading(page1);
    Resource<List<Shots>> p1Success = Resource.success(page1);

    List<Shots> page2 = Util.loadFromResource(getClass().getClassLoader(), mGson, "shots_page2_limit10.json", new TypeToken<List<Shots>>() { }.getType());
    Resource<List<Shots>> p2Loading = Resource.loading(page2);
    Resource<List<Shots>> p2Success = Resource.success(page2);

    Mockito.when(mRepository.read(anyInt())).thenReturn(Observable.just(p1Loading, p1Success));

    RecordingObserver<Resource<List<Shots>>> subscriber = mSubscriberRule.create();
    mViewModel.read().subscribe(subscriber);

    Mockito.verify(mRepository).read(10);
    subscriber.assertValue(p1Loading);
    subscriber.assertValue(p1Success);
    subscriber.assertComplete();

    Mockito.when(mRepository.readPage(1, 10)).thenReturn(Observable.just(p2Loading, p2Success));
    RecordingObserver<Resource<List<Shots>>> subscriberPage = mSubscriberRule.create();
    mViewModel.loadMore(1).subscribe(subscriberPage);

    Mockito.verify(mRepository).read(10);
    subscriberPage.assertValue(p2Loading);
    subscriberPage.assertValue(p2Success);
    subscriberPage.assertComplete();

  }

  @Test
  public void testErrorAuthentication(){
    ResponseBody body = ResponseBody.create(MediaType.parse("text/plain"), "{\n\"message\": \"Bad credentials.\"\n}");
    HttpException exception = new HttpException(Response.error(401, body));
    Mockito.when(mRepository.read(anyInt())).thenReturn(Observable.<Resource<List<Shots>>>error(exception));

    RecordingObserver<Resource<List<Shots>>> subscriber = mSubscriberRule.create();
    mViewModel.read().subscribe(subscriber);

    subscriber.assertValue(Resource.<List<Shots>>error("Authentication Error", null)).assertComplete();
  }

  @Test
  public void testErrorBadRequest(){
    ResponseBody body = ResponseBody.create(MediaType.parse("text/plain"), "{\n\"message\": \"Not found.\"\n}");
    HttpException exception = new HttpException(Response.error(404, body));
    Mockito.when(mRepository.read(anyInt())).thenReturn(Observable.<Resource<List<Shots>>>error(exception));

    RecordingObserver<Resource<List<Shots>>> subscriber = mSubscriberRule.create();
    mViewModel.read().subscribe(subscriber);

    subscriber.assertValue(Resource.<List<Shots>>error("Bad Request", null)).assertComplete();
  }

  @Test
  public void testErrorInternalServer(){
    ResponseBody body = ResponseBody.create(MediaType.parse("text/plain"), "{\n\"message\": \"Internal server error.\"\n}");
    HttpException exception = new HttpException(Response.error(500, body));
    Mockito.when(mRepository.read(anyInt())).thenReturn(Observable.<Resource<List<Shots>>>error(exception));

    RecordingObserver<Resource<List<Shots>>> subscriber = mSubscriberRule.create();
    mViewModel.read().subscribe(subscriber);

    subscriber.assertValue(Resource.<List<Shots>>error("Unknown", null)).assertComplete();
  }
}
