package com.bnsantos.dribble.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.bnsantos.dribble.app.BaseApp;
import com.bnsantos.dribble.di.components.DaggerAppComponent;
import com.bnsantos.dribble.di.modules.AppModule;

import dagger.android.AndroidInjection;

public class AppInjector {
  private AppInjector() {}

  public static void init(BaseApp app) {
    DaggerAppComponent.builder().application(app).build().inject(app);
    registerLyfecycleCallbacks(app);
  }

  public static void init(BaseApp app, AppModule module) {
    DaggerAppComponent.builder().application(app).setAppModule(module).build().inject(app);
    registerLyfecycleCallbacks(app);
  }

  private static void registerLyfecycleCallbacks(Application app) {
    app
        .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
          @Override
          public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            handleActivity(activity);
          }

          @Override
          public void onActivityStarted(Activity activity) {

          }

          @Override
          public void onActivityResumed(Activity activity) {

          }

          @Override
          public void onActivityPaused(Activity activity) {

          }

          @Override
          public void onActivityStopped(Activity activity) {

          }

          @Override
          public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

          }

          @Override
          public void onActivityDestroyed(Activity activity) {

          }
        });
  }

  private static void handleActivity(Activity activity) {
    AndroidInjection.inject(activity);
  }
}