package com.bnsantos.dribble.viewmodel;

import com.bnsantos.dribble.BuildConfig;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.repository.ShotsRepository;
import com.bnsantos.dribble.vo.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

@Singleton public class ShotsViewModel {
  private static final String TAG = ShotsViewModel.class.getSimpleName();
  private final ShotsRepository mRepo;

  @Inject public ShotsViewModel(ShotsRepository repo) {
    mRepo = repo;
  }

  public Observable<Resource<List<Shots>>> read(){
    return mRepo.read(BuildConfig.SHOTS_PAGE)
        .onErrorReturn(new Function<Throwable, Resource<List<Shots>>>() {
          @Override
          public Resource<List<Shots>> apply(@NonNull Throwable throwable) throws Exception {
            if(throwable instanceof HttpException) {
              int code = ((HttpException) throwable).code();
              switch (code){
                case 401:
                  return Resource.error("Authentication Error", null);
                case 404:
                  return Resource.error("Bad Request", null);
              }
            }
            return Resource.error("Unknown", null);
          }
        });
  }

  public Observable<Resource<List<Shots>>> loadMore(int page) {
    return mRepo.readPage(page, 10);
  }
}
