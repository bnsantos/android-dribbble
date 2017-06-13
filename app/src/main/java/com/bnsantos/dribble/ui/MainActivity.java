package com.bnsantos.dribble.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bnsantos.dribble.R;
import com.bnsantos.dribble.repository.ShotsRepository;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
  @Inject ShotsRepository mRepo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}
