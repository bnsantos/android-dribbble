package com.bnsantos.dribble.ui;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.bnsantos.dribble.R;
import com.bnsantos.dribble.util.RecyclerViewMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

  @Test
  public void test(){
    MainActivity activity = mActivityTestRule.getActivity();
    assertThat(activity.mAdapter.getItemCount()).isEqualTo(10);
    assertThat(activity.mRefreshLayout.isRefreshing()).isFalse();

    RecyclerViewMatcher recyclerViewMatcher = new RecyclerViewMatcher(R.id.recyclerView);

    onView(recyclerViewMatcher.atPosition(0)).check(matches(hasDescendant(withText("Dark Emperor"))));
    onView(recyclerViewMatcher.atPosition(2)).check(matches(hasDescendant(withText("Instalend pages - How it works  for Borrowers & Investors. "))));
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).perform(RecyclerViewActions.scrollToPosition(6));
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertThat(activity.mAdapter.getItemCount()).isEqualTo(20);
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).perform(RecyclerViewActions.scrollToPosition(12));
    onView(recyclerViewMatcher.atPosition(12)).check(matches(hasDescendant(withText("UXPin.com - Design Systems"))));
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).perform(RecyclerViewActions.scrollToPosition(17));
    onView(recyclerViewMatcher.atPosition(17)).check(matches(hasDescendant(withText("Pattern #4"))));
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertThat(activity.mAdapter.getItemCount()).isEqualTo(30);
  }
}
