package de.pcps.jamtugether.api.services;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.JoinRoomResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RoomService {

    @POST("create-room")
    Call<CreateRoomResponse> createRoom(@Body @NonNull CreateRoomBody body);

    @POST("login")
    Call<JoinRoomResponse> joinRoom(@Body @NonNull JoinRoomBody body);

    class CreateRoomBody {

        @NonNull
        private final String password;

        private final int roomID = 1; // todo remove later

        public CreateRoomBody(@NonNull String password) {
            this.password = password;
        }
    }

    class JoinRoomBody {

        @NonNull
        private final String password;

        private int roomID;

        public JoinRoomBody(int roomID, @NonNull String password) {
            this.password = password;
            this.roomID = roomID;
        }
    }
}

