package de.pcps.jamtugether;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ConnectionTests extends TestHelpers {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCreateRoomOffline() {
        // this test only works on devices running Android P or lower
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // disable wifi to make sure we're not connected to the server
        rule.getScenario().onActivity(activity -> setWifiEnabled(false, activity));
        WifiManager wifiManager = (WifiManager) appContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // check if disabling wifi was successful
        if (!wifiManager.isWifiEnabled()) {
            createRoomAndWait("Test", "abc", 5000);
            // we're offline so we should not connect
            onView(withText(R.string.no_internet_connection_message)).check(matches(isDisplayed()));
        }
        rule.getScenario().onActivity(activity -> setWifiEnabled(true, activity));
    }
}
