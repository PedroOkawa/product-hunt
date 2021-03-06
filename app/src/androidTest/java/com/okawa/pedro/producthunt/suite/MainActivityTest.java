package com.okawa.pedro.producthunt.suite;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.di.component.TestAppComponent;
import com.okawa.pedro.producthunt.ui.loading.LoadingActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.okawa.pedro.producthunt.matcher.RecyclerViewMatcher.withRecyclerView;
import static com.okawa.pedro.producthunt.matcher.ToolbarMatcher.matchToolbarTitle;
import static com.okawa.pedro.producthunt.util.TestHelper.*;

/**
 * Created by pokawa on 24/02/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<LoadingActivity> activityRule = new ActivityTestRule<>(LoadingActivity.class);

    @Before
    public void setup() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        ProductHuntApp app = (ProductHuntApp) instrumentation.getTargetContext().getApplicationContext();
        TestAppComponent component = (TestAppComponent) app.getAppComponent();
        component.inject(this);

        closeSoftKeyboard();
        sleep(SMALL_DELAY);
    }

    @Test
    public void validateTechPostsInteraction() {

        openNavigationOption(R.id.dlActivityMain, "Tech");

        sleep(HUGE_DELAY);

        for(int i = 0; i < TOTAL_SEARCH_TEST; i++) {

            sleep(SMALL_DELAY);

            String name = getText(withRecyclerView(R.id.rvActivityMainPosts)
                    .atPositionOnView(i, R.id.tvViewPostDetailsName));

            checkRecyclerItem(i, R.id.rvActivityMainPosts);

            matchToolbarTitle(name).check(matches(isDisplayed()));

            sleep(SMALL_DELAY);

            pressBack();
        }
    }

    @Test
    public void validateGamesPostsInteraction() {

        openNavigationOption(R.id.dlActivityMain, "Games");

        sleep(HUGE_DELAY);

        for(int i = 0; i < TOTAL_SEARCH_TEST; i++) {

            sleep(SMALL_DELAY);

            String name = getText(withRecyclerView(R.id.rvActivityMainPosts)
                    .atPositionOnView(i, R.id.tvViewPostDetailsName));

            checkRecyclerItem(i, R.id.rvActivityMainPosts);

            matchToolbarTitle(name).check(matches(isDisplayed()));

            sleep(SMALL_DELAY);

            pressBack();
        }
    }

    @Test
    public void validatePodcastsPostsInteraction() {

        openNavigationOption(R.id.dlActivityMain, "Podcasts");

        sleep(HUGE_DELAY);

        for(int i = 0; i < TOTAL_SEARCH_TEST; i++) {

            sleep(SMALL_DELAY);

            String name = getText(withRecyclerView(R.id.rvActivityMainPosts)
                    .atPositionOnView(i, R.id.tvViewPostDetailsName));

            checkRecyclerItem(i, R.id.rvActivityMainPosts);

            matchToolbarTitle(name).check(matches(isDisplayed()));

            sleep(SMALL_DELAY);

            pressBack();
        }
    }

    @Test
    public void validateBooksPostsInteraction() {

        openNavigationOption(R.id.dlActivityMain, "Books");

        sleep(HUGE_DELAY);

        for(int i = 0; i < TOTAL_SEARCH_TEST; i++) {

            sleep(SMALL_DELAY);

            String name = getText(withRecyclerView(R.id.rvActivityMainPosts)
                    .atPositionOnView(i, R.id.tvViewPostDetailsName));

            checkRecyclerItem(i, R.id.rvActivityMainPosts);

            matchToolbarTitle(name).check(matches(isDisplayed()));

            sleep(SMALL_DELAY);

            pressBack();
        }
    }

}
