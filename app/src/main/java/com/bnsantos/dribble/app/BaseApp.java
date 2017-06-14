package com.bnsantos.dribble.app;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class BaseApp extends Application implements HasActivityInjector {
  @Inject
  DispatchingAndroidInjector<Activity> mDispatchingAndroidInjector;

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return mDispatchingAndroidInjector;
  }
}
