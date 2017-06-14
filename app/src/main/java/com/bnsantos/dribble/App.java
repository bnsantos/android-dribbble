package com.bnsantos.dribble;

import android.app.Activity;
import android.app.Application;

import com.bnsantos.dribble.di.AppInjector;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class App extends Application implements HasActivityInjector{
  @Inject
  DispatchingAndroidInjector<Activity> mDispatchingAndroidInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    AppInjector.init(this);
    FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());
    Fresco.initialize(this);
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return mDispatchingAndroidInjector;
  }
}
