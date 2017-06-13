package com.bnsantos.dribble.api;

import android.support.annotation.NonNull;

import com.bnsantos.dribble.models.Shots;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DribbleService {
  @GET("/shots")
  Observable<List<Shots>> read(
      @NonNull final @Query(value = "page") int page,
      @NonNull final @Query(value = "per_page") int perPage,
      @NonNull final @Query(value = "sort") String sort,
      @NonNull final @Query(value = "access_token") String token);
}
