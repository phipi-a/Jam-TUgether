package de.pcps.jamtugether;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

@RunWith(AndroidJUnit4.class)
public class EdgeCaseTest extends TestHelpers {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testXSSPassword() {
        onView(withId(R.id.create_room_button))
                .perform(click());
        onView(withId(R.id.room_password_edit_text))
                .perform(replaceText ("foo\\x22console.log('XSS😈');//"));
        onView(withId(R.id.create_room_button))
                .perform(click());
        // wait 10s to ensure client is connected
        onView(isRoot()).perform(waitFor(10000));
    }

    @Test
    public void testLongPassword() {
        onView(withId(R.id.create_room_button))
                .perform(click());
        onView(withId(R.id.room_password_edit_text))
                .perform(typeText( new String(new char[99]).replace("\0", "a")));
        onView(withId(R.id.create_room_button))
                .perform(click());
        onView(withText(R.string.password_too_large_error_message)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyPassword() {
        onView(withId(R.id.create_room_button))
                .perform(click());
        onView(withId(R.id.create_room_button))
                .perform(click());
        onView(withText(R.string.password_input_empty)).check(matches(isDisplayed()));
    }
}