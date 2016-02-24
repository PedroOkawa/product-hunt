package com.okawa.pedro.producthunt.util;

import android.support.annotation.IdRes;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.TextView;

import com.okawa.pedro.producthunt.R;

import org.hamcrest.Matcher;

import greendao.Session;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by pokawa on 24/02/16.
 */
public class TestHelper {

    private static final String API_KEY_TEMP = "Bearer 7dac67657ca38f8204bd0298e5cea4dfb5ba190d1443f6fc9a278e15b7e154f1";

    public static final int INITIAL_DELAY = 10000;
    public static final int INTERACTION_DELAY = 1000;
    public static final int TOTAL_SEARCH_TEST = 5;

    public static void checkRecyclerItem(int position, @IdRes int layoutId) {
        onView(allOf(ViewMatchers.withId(layoutId), isDisplayed()))
                .perform(RecyclerViewActions.scrollToPosition(position),
                        RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }

    public static String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    public static void openNavigationOption(@IdRes int drawerLayout, String text) {
        openDrawer(drawerLayout);

        sleep(INTERACTION_DELAY);

        onView(withText(text)).perform(click());

        sleep(INTERACTION_DELAY);
    }

    public static Session generateSession() {
        Session session = new Session();
        session.setAccessToken(API_KEY_TEMP);
        return session;
    }

}
