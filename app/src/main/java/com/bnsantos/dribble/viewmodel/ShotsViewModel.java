package com.bnsantos.dribble.viewmodel;

import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.repository.ShotsRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton public class ShotsViewModel {
  private final ShotsRepository mRepo;

  @Inject public ShotsViewModel(ShotsRepository repo) {
    mRepo = repo;
  }

  public Observable<List<Shots>> read(){
    return mRepo.read(10);
  }
}
