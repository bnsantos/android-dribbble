package com.bnsantos.dribble.ui;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bnsantos.dribble.R;
import com.bnsantos.dribble.models.Shots;
import com.bnsantos.dribble.models.User;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ShotActivityTest {
  @Rule
  public ActivityTestRule<ShotActivity> mActivityTestRule = new ActivityTestRule<ShotActivity>(ShotActivity.class){
    @Override
    protected Intent getActivityIntent() {
      mShot = new Shots(
          123456,
          "title-test",
          null,
          100,
          100,
          null,
          10,
          100,
          null,
          null,
          null,
          false,
          new User(654321, "User Test 1", null, "Bio test", "My Location", null, "My Twitter", 10, 20, false, null, null));

      Intent intent = new Intent();
      intent.putExtra(ShotActivity.SHOT_PARAM, mShot);
      return intent;
    }
  };

  private Shots mShot;

  @Test
  public void testDisplayInfo() {
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    User creator = mShot.getCreator();
    ViewInteraction textView = onView(
        allOf(withId(R.id.name), withText(creator.getName()),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                    1),
                1),
            isDisplayed()));
    textView.check(matches(withText(creator.getName())));

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.location), withText(creator.getLocation()),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                    1),
                2),
            isDisplayed()));
    textView2.check(matches(withText(creator.getLocation())));

    ViewInteraction textView3 = onView(
        allOf(withId(R.id.web), withText(R.string.no_web),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                    1),
                3),
            isDisplayed()));
    textView3.check(matches(withText(R.string.no_web)));

    ViewInteraction textView4 = onView(
        allOf(withId(R.id.twitter), withText(creator.getTwitter()),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                    1),
                5),
            isDisplayed()));
    textView4.check(matches(withText(creator.getTwitter())));

    ViewInteraction textView5 = onView(
        allOf(withId(R.id.followers), withText(getResourceString(R.string.followers_d, creator.getFollowers())),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                    1),
                6),
            isDisplayed()));
    textView5.check(matches(withText(getResourceString(R.string.followers_d, creator.getFollowers()))));

    ViewInteraction textView6 = onView(
        allOf(withId(R.id.likesCount), withText(getResourceString(R.string.likes_d, creator.getLikes())),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                    1),
                7),
            isDisplayed()));
    textView6.check(matches(withText(getResourceString(R.string.likes_d, creator.getLikes()))));

    ViewInteraction textView7 = onView(
        allOf(withId(R.id.bio), withText(creator.getBio()),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                    1),
                9),
            isDisplayed()));
    textView7.check(matches(withText(creator.getBio())));

    ViewInteraction textView8 = onView(
        allOf(withId(R.id.title), withText(mShot.getTitle()),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                    1),
                0),
            isDisplayed()));
    textView8.check(matches(withText(mShot.getTitle())));

    ViewInteraction textView9 = onView(
        allOf(withId(R.id.likes), withText(Long.toString(mShot.getLikes())),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                    1),
                1),
            isDisplayed()));
    textView9.check(matches(withText(Long.toString(mShot.getLikes()))));

    ViewInteraction textView10 = onView(
        allOf(withId(R.id.views), withText(Long.toString(mShot.getViews())),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                    1),
                2),
            isDisplayed()));
    textView10.check(matches(withText(Long.toString(mShot.getViews()))));

  }

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }

  private String getResourceString(int id, long d) {
    Context targetContext = InstrumentationRegistry.getTargetContext();
    return targetContext.getResources().getString(id, d);
  }
}
