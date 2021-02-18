package de.pcps.jamtugether;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.Espresso;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ServerConnect extends TestHelpers {
    static private String PASSWORD = "foo";
    static private String NAME = "Test";

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void testCreateRoom() {
//        ActivityScenario scenario = rule.getScenario();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        createRoomAndWait(NAME, PASSWORD);
        Log.i("Activity", appContext.getApplicationContext().toString());
//        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsAdmin() {
//        onView(withParentIndex(R.id.tab_layout)).perform(click());
//        onView(withText(R.string.soundtrack_over_view)).perform(click());
        onView(withId(R.id.delete_room_text_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsRoomEmpty() {
        onView(withId(R.id.view_pager)).perform(swipeRight());
        // a new room should not have any tracks
        onView(withId(R.id.all_soundtracks_recycler_view)).check(matches(hasChildCount(0)));
    }

    @After
    public void testCloseRoom() {
//        onView(withText(R.string.soundtrack_over_view)).perform(click());
//        onView(withId(R.id.delete_room_text_view)).perform(click());
        rule.getScenario().close();
    }
}