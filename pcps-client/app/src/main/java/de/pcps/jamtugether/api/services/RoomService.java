package de.pcps.jamtugether.api.services;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.JoinRoomResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// todo add actual endpoints with correct response classes
public interface RoomService {

    @POST("create-room")
    Call<CreateRoomResponse> createRoom(@Body CreateRoomBody body);

    @POST("login")
    Call<JoinRoomResponse> joinRoom(@Body JoinRoomBody body);

    public class CreateRoomBody {
        private String password;
        private int roomID = 1;

        public CreateRoomBody(@NonNull String password) {
            this.password = password;
        }
    }

    public class JoinRoomBody {
        private String password;
        private int roomID;

        public JoinRoomBody(int roomID, @NonNull String password) {
            this.password = password;
            this.roomID = roomID;
        }
    }
}

