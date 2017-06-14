package com.bnsantos.dribble.db;

import android.support.annotation.NonNull;

import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.Shots_Table;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ShotsDaoImpl implements ShotsDao {
  @Override
  public Observable<List<Shots>> read(final int limit) {
    return RXSQLite.rx(
        SQLite.select()
            .from(Shots.class)
            .orderBy(Shots_Table.views, false)
            .limit(limit)
    )
    .queryList()
    .toObservable();
  }

  @Override
  public Observable<List<Shots>> cache(@NonNull final List<Shots> shotsList) {
    return Observable.create(new ObservableOnSubscribe<List<Shots>>() {
      @Override
      public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<Shots>> e) throws Exception {
        for (Shots shots : shotsList) {
          shots.save();
        }
        e.onNext(shotsList);
        e.onComplete();
      }
    });
  }
}
