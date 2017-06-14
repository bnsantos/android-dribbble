package com.bnsantos.dribble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bnsantos.dribble.R;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.ui.recyclerview.EndlessRecyclerViewScrollListener;
import com.bnsantos.dribble.ui.recyclerview.RecyclerItemClickListener;
import com.bnsantos.dribble.viewmodel.ShotsViewModel;
import com.bnsantos.dribble.vo.Resource;
import com.bnsantos.dribble.vo.Status;
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
  protected EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;

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
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
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

    mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        Log.i(TAG, "LoadMore: " + page);
        mRefreshLayout.setRefreshing(true);
        loadMore(page);
      }
    };
    recyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);

    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        read();
        mEndlessRecyclerViewScrollListener.resetState();
      }
    });

    read();
  }

  protected void read() {
    mViewModel.read()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.<Resource<List<Shots>>>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(new Consumer<Resource<List<Shots>>>() {
          @Override
          public void accept(@NonNull Resource<List<Shots>> resource) throws Exception {
            mRefreshLayout.setRefreshing(false);
            if (resource.data != null) {
              mAdapter.setItems(resource.data);
            }
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

  protected void loadMore(final int page){
    mViewModel.loadMore(page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.<Resource<List<Shots>>>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(new Consumer<Resource<List<Shots>>>() {
          @Override
          public void accept(@NonNull Resource<List<Shots>> resource) throws Exception {
            if (resource.data != null) {
              if (resource.data.size() > 0) {
                mRefreshLayout.setRefreshing(false);
              }
              if (resource.status == Status.LOADING) {
                mAdapter.append(resource.data);
              }else {
                mAdapter.swap(page, resource.data);
              }
            }
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
