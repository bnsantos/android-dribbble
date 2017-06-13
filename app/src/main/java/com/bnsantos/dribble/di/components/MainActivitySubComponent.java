package com.bnsantos.dribble.di.components;

import com.bnsantos.dribble.ui.MainActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent public interface MainActivitySubComponent  extends AndroidInjector<MainActivity> {
  @Subcomponent.Builder
  public abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
}
