package com.bnsantos.dribble.repository;

import android.util.Log;

import com.bnsantos.dribble.BuildConfig;
import com.bnsantos.dribble.api.DribbleService;
import com.bnsantos.dribble.db.ShotsDao;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.vo.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

@Singleton public class ShotsRepository {
  private static final String TAG = ShotsRepository.class.getSimpleName();
  private final DribbleService mService;
  private final ShotsDao mDao;

  @Inject
  public ShotsRepository(DribbleService service, ShotsDao dao) {
    mService = service;
    mDao = dao;
  }

  public Observable<Resource<List<Shots>>> read(final int max){
    Log.i(TAG, "read-> max=" +max);
    return Observable.mergeDelayError(
        readCached(0, max),
        readServer(0, max, "views")
    );
  }

  private Observable<Resource<List<Shots>>> readCached(final int offset, final int max){
    Log.i(TAG, "readCached-> offset=" + offset + ", max=" + max);
    return mDao.read(offset, max);
  }

  private Observable<Resource<List<Shots>>> readServer(final int page, final int max, @android.support.annotation.NonNull final String sort){
    Log.i(TAG, "readServer-> page=" + (page+1) + ", max="+max + ", sort="+sort);
    return mService.read(page+1, max, sort, BuildConfig.DRIBBBLE_TOKEN)
        .flatMap(new Function<List<Shots>, Observable<Resource<List<Shots>>>>() {
          @Override
          public Observable<Resource<List<Shots>>> apply(@NonNull List<Shots> shotsList) throws Exception {
            return mDao.cache(shotsList);
          }
        });
  }

  public Observable<Resource<List<Shots>>> readPage(final int page, final int max){
    Log.i(TAG, "readPage-> page=" + (page) + ", max="+max );
    return Observable.mergeDelayError(
        readCached(page*max, max),
        readServer(page, max, "views")
    );
  }
}
