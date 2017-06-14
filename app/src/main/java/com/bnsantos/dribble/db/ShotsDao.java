package com.bnsantos.dribble.db;

import android.support.annotation.NonNull;

import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.vo.Resource;

import java.util.List;

import io.reactivex.Observable;

public interface ShotsDao {
  Observable<Resource<List<Shots>>> read(final int offset, final int limit);

  Observable<Resource<List<Shots>>> cache(@NonNull final List<Shots> shotsList);
}
