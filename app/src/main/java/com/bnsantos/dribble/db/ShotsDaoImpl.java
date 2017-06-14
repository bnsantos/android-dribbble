package com.bnsantos.dribble.db;

import android.support.annotation.NonNull;

import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.Shots_Table;
import com.bnsantos.dribble.vo.Resource;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class ShotsDaoImpl implements ShotsDao {
  @Override
  public Observable<Resource<List<Shots>>> read(final int offset, final int limit) {
    return RXSQLite.rx(
        SQLite.select()
            .from(Shots.class)
            .orderBy(Shots_Table.views, false)
            .offset(offset)
            .limit(limit)
    )
    .queryList()
    .toObservable()
    .flatMap(new Function<List<Shots>, ObservableSource<Resource<List<Shots>>>>() {
      @Override
      public ObservableSource<Resource<List<Shots>>> apply(@io.reactivex.annotations.NonNull List<Shots> shotsList) throws Exception {
        return Observable.just(Resource.loading(shotsList));
      }
    });
  }

  @Override
  public Observable<Resource<List<Shots>>> cache(@NonNull final List<Shots> shotsList) {
    return Observable.create(new ObservableOnSubscribe<Resource<List<Shots>>>() {
      @Override
      public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Resource<List<Shots>>> e) throws Exception {
        for (Shots shots : shotsList) {
          shots.save();
        }
        e.onNext(Resource.success(shotsList));
        e.onComplete();
      }
    });
  }
}
