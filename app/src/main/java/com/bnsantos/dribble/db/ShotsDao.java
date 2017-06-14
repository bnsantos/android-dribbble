package com.bnsantos.dribble.db;

import android.support.annotation.NonNull;

import com.bnsantos.dribble.models.Shots;

import java.util.List;

import io.reactivex.Observable;

public interface ShotsDao {
  Observable<List<Shots>> read(final int limit);

  Observable<List<Shots>> cache(@NonNull final List<Shots> shotsList);
}
