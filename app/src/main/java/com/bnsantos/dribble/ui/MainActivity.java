package com.bnsantos.dribble.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bnsantos.dribble.R;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.viewmodel.ShotsViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  @Inject ShotsViewModel mViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*mViewModel.read()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Shots>>() {
          @Override
          public void accept(@NonNull List<Shots> shotsList) throws Exception {
            Log.i(TAG, shotsList.toString());
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(@NonNull Throwable throwable) throws Exception {
            Log.e(TAG, "Error", throwable);

          }
        }, new Action() {
          @Override
          public void run() throws Exception {
            Log.i(TAG, "oncompleted");
          }
        });*/
  }
}
