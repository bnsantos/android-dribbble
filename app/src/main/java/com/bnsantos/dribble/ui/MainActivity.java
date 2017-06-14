package com.bnsantos.dribble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.bnsantos.dribble.R;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.viewmodel.ShotsViewModel;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bnsantos.dribble.ui.ShotActivity.SHOT_PARAM;

public class MainActivity extends RxAppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  @Inject ShotsViewModel mViewModel;
  protected SwipeRefreshLayout mRefreshLayout;
  protected ShotsAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int windowWidth = displayMetrics.widthPixels;
    mAdapter = new ShotsAdapter((int) (windowWidth * 0.7), (int) (getResources().getDimensionPixelOffset(R.dimen.cover_height) * 0.7));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(mAdapter);
    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
      @Override
      public void onItemClick(View view, int position) {
        Shots item = mAdapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, ShotActivity.class);
        intent.putExtra(SHOT_PARAM, item);
        startActivity(intent);
      }
    }));

    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        read();
      }
    });

    read();
  }

  protected void read() {
    mViewModel.read()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.<List<Shots>>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(new Consumer<List<Shots>>() {
          @Override
          public void accept(@NonNull List<Shots> shotsList) throws Exception {
            mRefreshLayout.setRefreshing(false);
            mAdapter.swap(shotsList);
            mAdapter.notifyDataSetChanged();
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(@NonNull Throwable throwable) throws Exception {
            mRefreshLayout.setRefreshing(false);
            Log.e(TAG, "Error", throwable);

          }
        }, new Action() {
          @Override
          public void run() throws Exception {
            Log.i(TAG, "oncompleted");
          }
        });
  }
}
