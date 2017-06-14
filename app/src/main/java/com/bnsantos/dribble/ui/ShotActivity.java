package com.bnsantos.dribble.ui;

import android.os.Build;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bnsantos.dribble.R;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;
import com.bnsantos.dribble.utils.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Locale;

public class ShotActivity extends AppCompatActivity {
  private static final String TAG = ShotActivity.class.getSimpleName();
  public static final String SHOT_PARAM = "shot";

  private ShotsAdapter.ShotsHolder mHolder;

  private SimpleDraweeView mAvatar;
  private TextView mName;
  private TextView mBio;
  private TextView mLocation;
  private TextView mWeb;
  private TextView mTwitter;
  private TextView mFollowers;
  private TextView mLikes;
  private Shots mShots;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shot);

    if (getIntent() != null && getIntent().hasExtra(SHOT_PARAM)) {
      mShots = getIntent().getParcelableExtra(SHOT_PARAM);
    }else if(savedInstanceState != null && savedInstanceState.containsKey(SHOT_PARAM)){
      mShots = savedInstanceState.getParcelable(SHOT_PARAM);
    }

    mHolder = new ShotsAdapter.ShotsHolder(findViewById(R.id.header));
    mAvatar = (SimpleDraweeView) findViewById(R.id.avatar);
    mName = (TextView) findViewById(R.id.name);
    mBio = (TextView) findViewById(R.id.bio);
    mLocation = (TextView) findViewById(R.id.location);
    mWeb = (TextView) findViewById(R.id.web);
    mTwitter = (TextView) findViewById(R.id.twitter);
    mFollowers = (TextView) findViewById(R.id.followers);
    mLikes = (TextView) findViewById(R.id.likesCount);

    updateViews(mShots);
  }

  private void updateViews(@NonNull final Shots shots) {
    mHolder.onBind(shots, shots.getWidth(), shots.getHeight());
    User creator = shots.getCreator();
    if (creator.getAvatar() != null) {
      int size = getResources().getDimensionPixelSize(R.dimen.avatar_size);
      FrescoUtils.load(mAvatar, creator.getAvatar(), size, size, false);
    }else {
      mAvatar.setImageResource(android.R.color.transparent);
    }

    mName.setText(creator.getName());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      mBio.setText(Html.fromHtml(creator.getBio(), Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
    }else {
      mBio.setText(Html.fromHtml(creator.getBio()), TextView.BufferType.SPANNABLE);
    }

    if (creator.getLocation() != null) {
      mLocation.setText(creator.getLocation());
    }else {
      mLocation.setText(R.string.no_location);
    }

    if (creator.getWeb() != null) {
      mWeb.setText(creator.getWeb());
    }else {
      mWeb.setText(R.string.no_web);
    }

    if (creator.getTwitter() != null) {
      mTwitter.setText(creator.getTwitter());
    }else {
      mTwitter.setText(R.string.no_twitter);
    }

    mLikes.setText(getString(R.string.likes_d, creator.getLikes()));
    mFollowers.setText(getString(R.string.followers_d, creator.getFollowers()));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(SHOT_PARAM, mShots);
  }
}
