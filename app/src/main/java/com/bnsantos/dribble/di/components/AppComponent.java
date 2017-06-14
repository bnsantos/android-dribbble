package com.bnsantos.dribble.di.components;

import android.app.Application;

import com.bnsantos.dribble.app.BaseApp;
import com.bnsantos.dribble.di.modules.AppModule;
import com.bnsantos.dribble.di.modules.MainActivityModule;
import com.bnsantos.dribble.di.modules.ShotActivityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@dagger.Component(modules = {
    AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class,
    AppModule.class,
    MainActivityModule.class,
    ShotActivityModule.class
})
public interface AppComponent {
  @dagger.Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application application);
    Builder setAppModule(AppModule module);
    AppComponent build();
  }
  void inject(BaseApp app);
}
