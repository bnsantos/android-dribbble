package com.bnsantos.dribble.di.modules;

import android.app.Activity;

import com.bnsantos.dribble.di.components.ShotActivitySubComponent;
import com.bnsantos.dribble.ui.ShotActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = ShotActivitySubComponent.class)
public abstract class ShotActivityModule {
  @Binds
  @IntoMap
  @ActivityKey(ShotActivity.class)
  abstract AndroidInjector.Factory<? extends Activity> bindShotActivityInjectorFactory(ShotActivitySubComponent.Builder builder);
}
