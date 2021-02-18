package de.pcps.jamtugether;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ServerConnect extends TestHelpers {
    static private final String PASSWORD = "foo";

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void testCreateRoom() {
//        ActivityScenario scenario = rule.getScenario();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.create_room_button))
                .perform(click());
        onView(withId(R.id.room_password_edit_text))
                .perform(typeText(PASSWORD));
        onView(withId(R.id.create_room_button))
                .perform(click());
        // wait 10s to ensure client is connected
        onView(isRoot()).perform(waitFor(10000));
        Log.i("Activity", appContext.getApplicationContext().toString());
        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsAdmin() {
        onView(withId(R.id.delete_room_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsRoomEmpty() {
        // a new room should not have any tracks
        onView(withId(R.id.all_soundtracks_recycler_view)).check(matches(hasChildCount(0)));
    }

    @After
    public void testCloseRoom() {
        onView(withId(R.id.delete_room_button))
                .perform(click());
//        rule.getScenario().close();
    }
}