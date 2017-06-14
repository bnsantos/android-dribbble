package com.bnsantos.dribble.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnsantos.dribble.utils.FrescoUtils;
import com.bnsantos.dribble.R;
import com.bnsantos.dribble.models.Shots;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShotsAdapter extends RecyclerView.Adapter<ShotsAdapter.ShotsHolder> {
  private final List<Shots> mItems;
  private final int mWidth;
  private final int mHeight;

  public ShotsAdapter(int w, int h) {
    mItems = new ArrayList<>();
    mWidth = w;
    mHeight = h;
  }

  @Override
  public ShotsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shots, parent, false);
    return new ShotsHolder(view);
  }

  @Override
  public void onBindViewHolder(ShotsHolder holder, int position) {
    holder.onBind(mItems.get(position), mWidth, mHeight);
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public void swap(List<Shots> items){
    mItems.clear();
    mItems.addAll(items);
  }

  public Shots getItem(int position) {
    return mItems.get(position);
  }

  static class ShotsHolder extends RecyclerView.ViewHolder {
    private final SimpleDraweeView cover;
    private final TextView title;
    private final TextView views;
    private final TextView likes;

    ShotsHolder(View itemView) {
      super(itemView);
      cover = (SimpleDraweeView) itemView.findViewById(R.id.cover);
      title = (TextView) itemView.findViewById(R.id.title);
      views = (TextView) itemView.findViewById(R.id.views);
      likes = (TextView) itemView.findViewById(R.id.likes);
    }

    public void onBind(@NonNull final Shots shots, final int w, final int h){
      title.setText(shots.getTitle());
      views.setText(String.format(Locale.getDefault(), "%d", shots.getViews()));
      likes.setText(String.format(Locale.getDefault(), "%d", shots.getLikes()));

      if (shots.getImage() != null) {
        FrescoUtils.load(cover, shots.getImage(), w, h, shots.isAnimated());
      }else {
        cover.setImageResource(android.R.color.transparent);
      }
    }
  }
}
