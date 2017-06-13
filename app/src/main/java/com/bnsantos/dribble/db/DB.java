package com.bnsantos.dribble.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = DB.NAME, version = DB.VERSION, foreignKeyConstraintsEnforced = true)
public class DB {
  static final String NAME = "dribbble";
  static final int VERSION = 1;
}
