package com.bnsantos.dribble.app;

import com.bnsantos.dribble.di.AppInjector;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class App extends BaseApp {
  @Override
  public void onCreate() {
    super.onCreate();
    AppInjector.init(this);
    FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());
    Fresco.initialize(this);
  }
}
