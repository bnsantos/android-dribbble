package com.bnsantos.dribble.repository;

import com.bnsantos.dribble.BuildConfig;
import com.bnsantos.dribble.api.DribbleService;
import com.bnsantos.dribble.db.ShotsDao;
import com.bnsantos.dribble.models.Shots;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

@Singleton public class ShotsRepository {
  private final DribbleService mService;
  private final ShotsDao mDao;

  @Inject
  public ShotsRepository(DribbleService service, ShotsDao dao) {
    mService = service;
    mDao = dao;
  }

  public Observable<List<Shots>> read(){
    return Observable.mergeDelayError(
        readCached(),
        readServer()
    );
  }

  private Observable<List<Shots>> readCached(){
    return mDao.read();
  }

  private Observable<List<Shots>> readServer(){
    return mService.read(1, 10, "views", BuildConfig.DRIBBBLE_TOKEN)
        .flatMap(new Function<List<Shots>, Observable<List<Shots>>>() {
          @Override
          public Observable<List<Shots>> apply(@NonNull List<Shots> shotsList) throws Exception {
            return mDao.cache(shotsList);
          }
        });
  }
}
