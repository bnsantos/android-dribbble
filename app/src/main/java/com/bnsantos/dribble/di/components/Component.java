package com.bnsantos.dribble.di.components;

import android.app.Application;

import com.bnsantos.dribble.App;

import javax.inject.Singleton;

import dagger.BindsInstance;

@Singleton
@dagger.Component(modules = {

})
public interface Component {
  @dagger.Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application application);
    Component build();
  }
  void inject(App app);
}
