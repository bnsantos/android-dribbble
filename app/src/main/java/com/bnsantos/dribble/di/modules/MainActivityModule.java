package com.bnsantos.dribble.di.modules;

import android.app.Activity;

import com.bnsantos.dribble.di.components.MainActivitySubComponent;
import com.bnsantos.dribble.ui.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = MainActivitySubComponent.class)
public abstract class MainActivityModule {
  @Binds
  @IntoMap
  @ActivityKey(MainActivity.class)
  abstract AndroidInjector.Factory<? extends Activity>
  bindMainActivityInjectorFactory(MainActivitySubComponent.Builder builder);
}
