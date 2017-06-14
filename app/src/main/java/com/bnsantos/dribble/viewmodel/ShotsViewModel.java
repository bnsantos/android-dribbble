package com.bnsantos.dribble.viewmodel;

import android.util.Log;

import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.repository.ShotsRepository;
import com.bnsantos.dribble.vo.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

@Singleton public class ShotsViewModel {
  private static final String TAG = ShotsViewModel.class.getSimpleName();
  private final ShotsRepository mRepo;

  @Inject public ShotsViewModel(ShotsRepository repo) {
    mRepo = repo;
  }

  public Observable<Resource<List<Shots>>> read(){
    return mRepo.read(10)
        .doOnError(new Consumer<Throwable>() {
          @Override
          public void accept(@NonNull Throwable throwable) throws Exception {
            Log.e(TAG, "Test", throwable);
          }
        });
  }

  public Observable<Resource<List<Shots>>> loadMore(int page) {
    return mRepo.readPage(page, 10);
  }
}
