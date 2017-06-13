package com.bnsantos.dribble.api;

import com.bnsantos.dribble.api.deserializers.DateDeserializer;
import com.bnsantos.dribble.api.deserializers.ShotsDeserializer;
import com.bnsantos.dribble.api.deserializers.UserDeserializer;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;
import com.bnsantos.dribble.utils.RecordingObserver;
import com.bnsantos.dribble.utils.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class DribbleServiceTest {
  @Rule public MockWebServer mMockWebServer;
  @Rule public RecordingObserver.Rule mSubscriberRule = new RecordingObserver.Rule();
  private DribbleService mService;
  private Gson mGson;

  @Before
  public void createService() throws IOException {
    mMockWebServer = new MockWebServer();
    mGson = new GsonBuilder()
        .registerTypeAdapter(Date.class, new DateDeserializer())
        .registerTypeAdapter(User.class, new UserDeserializer())
        .registerTypeAdapter(Shots.class, new ShotsDeserializer())
        .create();
    mService = new Retrofit.Builder()
        .baseUrl(mMockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create(mGson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DribbleService.class);
  }

  @After
  public void stopService() throws IOException {
    mMockWebServer.shutdown();
  }

  @Test
  public void testRead() throws IOException{
    enqueueResponse("shots.json");
    RecordingObserver<List<Shots>> subscriber = mSubscriberRule.create();

    mService.read(1, 10, "views", "token").subscribe(subscriber);
    List<Shots> data = (List<Shots>) Util.loadFromResource(getClass().getClassLoader(), mGson, "shots.json", new TypeToken<List<Shots>>() {
    }.getType());

    List<Shots> response = subscriber.takeValue();

    assertThat(response).isNotNull();
    assertThat(response.size()).isEqualTo(10);
    assertThat(response).isEqualTo(data);
    subscriber.assertComplete();
  }

  @Test
  public void testInvalidToken() throws IOException {
    mMockWebServer.enqueue(new MockResponse().setResponseCode(401).setBody("{\n\"message\": \"Bad credentials.\"\n}"));
    RecordingObserver<List<Shots>> subscriber = mSubscriberRule.create();

    mService.read(1, 10, "views", "invalid_token").subscribe(subscriber);
    subscriber.assertError(HttpException.class, "HTTP 401 Client Error");
  }

  private void enqueueResponse(String fileName) throws IOException {
    enqueueResponse(fileName, Collections.<String, String>emptyMap());
  }

  private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("api-response/" + fileName);
    BufferedSource source = Okio.buffer(Okio.source(inputStream));
    MockResponse mockResponse = new MockResponse();
    for (Map.Entry<String, String> header : headers.entrySet()) {
      mockResponse.addHeader(header.getKey(), header.getValue());
    }
    mMockWebServer.enqueue(mockResponse.setBody(source.readString(Charset.forName("UTF-8"))));
  }
}
