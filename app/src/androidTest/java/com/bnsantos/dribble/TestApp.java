package com.bnsantos.dribble;

import com.bnsantos.dribble.app.BaseApp;
import com.bnsantos.dribble.di.AppInjector;
import com.bnsantos.dribble.di.modules.TestModule;
import com.facebook.drawee.backends.pipeline.Fresco;


public class TestApp extends BaseApp {
  @Override
  public void onCreate() {
    super.onCreate();
    Fresco.initialize(this);
    AppInjector.init(this, new TestModule());
  }
}
