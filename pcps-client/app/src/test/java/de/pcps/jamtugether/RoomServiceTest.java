package de.pcps.jamtugether;

import androidx.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.requests.room.responses.CreateRoomResponse;
import de.pcps.jamtugether.api.requests.room.RoomService;
import de.pcps.jamtugether.api.requests.room.bodies.CreateRoomBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RoomServiceTest {

    private int roomID;
    private String token;

    private RoomService roomService;
    private OkHttpClient client;

    @Before
    public void setUp() {
        // add callTimeout(1, TimeUnit.MILLISECONDS) to simulate disconnects
        client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
        roomService = retrofit.create(RoomService.class);
    }

    @Test
    public void createRoomTest() {
        createRoom("hello");
    }

    @Test
    public void createRoomTestWithCallback() {
        createRoom("abc", new JamCallback<CreateRoomResponse>() {
            @Override
            public void onSuccess(@NonNull CreateRoomResponse response) {
                roomID = response.getRoomID();
                token = response.getToken();
            }

            @Override
            public void onError(@NonNull Error error) {
                fail("createRoom returned an error: " + error.toString());
            }
        });
    }

    // callback version
    private void createRoom(@NonNull String password, @NonNull JamCallback<CreateRoomResponse> callback) {
        CreateRoomBody body = new CreateRoomBody(password);
        Call<CreateRoomResponse> call = roomService.createRoom(body);
        call.enqueue(callback);
    }

    // non-callback version
    private void createRoom(@NonNull String password) {
        CreateRoomBody body = new CreateRoomBody(password);
        Call<CreateRoomResponse> call = roomService.createRoom(body);
        try {
            Response<CreateRoomResponse> response = call.execute();
            CreateRoomResponse createRoomResponse = response.body();

            assertNotNull(createRoomResponse.getToken());
            assertFalse(createRoomResponse.getToken().isEmpty());
            assertTrue(createRoomResponse.getRoomID() > 0);

        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getLocalizedMessage());
        }
    }

    private void simulateDisconnect() {
        client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MILLISECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
        roomService = retrofit.create(RoomService.class);
    }

    private void simulateReconnect() {
        setUp();
    }
}
