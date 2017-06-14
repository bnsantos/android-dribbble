package com.bnsantos.dribble.di.components;

import com.bnsantos.dribble.ui.ShotActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent public interface ShotActivitySubComponent extends AndroidInjector<ShotActivity> {
  @Subcomponent.Builder
  public abstract class Builder extends AndroidInjector.Builder<ShotActivity> {}
}