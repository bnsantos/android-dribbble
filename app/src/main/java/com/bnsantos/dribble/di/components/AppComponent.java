package com.bnsantos.dribble.di.components;

import android.app.Application;

import com.bnsantos.dribble.App;
import com.bnsantos.dribble.di.modules.AppModule;
import com.bnsantos.dribble.di.modules.MainActivityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@dagger.Component(modules = {
    AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class,
    AppModule.class,
    MainActivityModule.class
//    MainActivityModule.class
})
public interface AppComponent {
  @dagger.Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application application);
    AppComponent build();
  }
  void inject(App app);
}