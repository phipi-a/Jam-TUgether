package de.pcps.jamtugether;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.responses.room.CreateRoomResponse;
import de.pcps.jamtugether.api.services.room.RoomService;
import de.pcps.jamtugether.api.services.room.bodies.CreateRoomBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class EdgeCaseTest extends TestHelpers {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testXSSPassword() {
        onView(withId(R.id.create_room_button))
                .perform(click());
        onView(withId(R.id.user_name_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.room_password_edit_text))
                .perform(replaceText("foo\\x22console.log('XSS😈');//"));
        onView(withId(R.id.create_room_button))
                .perform(scrollTo(),click());
        // wait 10s to ensure client is connected
        onView(isRoot()).perform(waitFor(10000));
    }

    @Test
    public void testLongPassword() {
        createRoomAndWait("Test", new String(new char[99]).replace("\0", "a"), 500);
        onView(withText(R.string.password_too_large_error_message)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyName() {
        createRoomAndWait("", "foo", 500);
        onView(withText(R.string.name_input_empty)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyPassword() {
        createRoomAndWait("Test", "", 500);
        onView(withText(R.string.password_input_empty)).check(matches(isDisplayed()));
    }

    @Test
    public void testTab() {
        createRoomAndWait("Test", "foo", 2000);

//        onView(allOf(withText("OVERVIEW"), isDescendantOfA(withId(R.id.tab_layout)))).perform(click());
//        onView(withText("OVERVIEW")).perform(click());
//        onView(withId(R.id.tab_layout)).perform(selectTab(0));
        onView(withChild(withText(R.string.soundtrack_over_view))).perform(scrollTo(), click());
//        onView(hasDescendant(withId(R.id.soundtrack_container))).perform(click());
//        onView(allOf(withText(R.string.soundtrack_over_view),
//                isDescendantOfA(withId(R.id.tab_layout)))).perform(click());
//        ViewInteraction tabView = onView(
//                allOf(childAtPosition(
//                        childAtPosition(
//                                withId(R.id.tab_layout),
//                                0),
//                        0),
//                        isDisplayed()));
//        tabView.perform(click());
//        onView(withId(R.id.view_pager)).perform(swipeLeft());
//        onView(withId(R.id.view_pager)).perform(swipeRight());

        onView(withText(R.string.delete_room)).check(matches(isDisplayed()));
    }

    @Test
    public void testJoinRoom() {
        int roomID = 0;
        String PASSWORD = "abc";

        // make api call to create room
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
        RoomService roomService = retrofit.create(RoomService.class);

        // save room id from response
        CreateRoomBody body = new CreateRoomBody(PASSWORD);
        Call<CreateRoomResponse> call = roomService.createRoom(body);
        try {
            Response<CreateRoomResponse> response = call.execute();
            CreateRoomResponse createRoomResponse = response.body();

            assertNotNull(createRoomResponse.getToken());
            assertFalse(createRoomResponse.getToken().isEmpty());
            assertTrue(createRoomResponse.getRoomID() > 0);
            roomID = createRoomResponse.getRoomID();

        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getLocalizedMessage());
        }

        // join room in espresso
        onView(withId(R.id.join_room_button)).perform(click());
        onView(withId(R.id.user_name_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.room_password_edit_text)).perform(typeText(PASSWORD));
        onView(withId(R.id.room_id_edit_text)).perform((typeText(String.valueOf(roomID))));
        closeSoftKeyboard();
        onView(withText(R.string.go_to_room)).perform(scrollTo(), click());
    }
}