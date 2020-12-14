package de.pcps.jamtugether.api.services.room;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.room.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.room.DeleteRoomResponse;
import de.pcps.jamtugether.api.responses.room.JoinRoomResponse;
import de.pcps.jamtugether.api.services.room.bodies.CreateRoomBody;
import de.pcps.jamtugether.api.services.room.bodies.DeleteRoomBody;
import de.pcps.jamtugether.api.services.room.bodies.JoinRoomBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RoomService {

    @POST("create-room")
    Call<CreateRoomResponse> createRoom(@Body @NonNull CreateRoomBody body);

    @POST("login")
    Call<JoinRoomResponse> joinRoom(@Body @NonNull JoinRoomBody body);

    @HTTP(method = "DELETE", path = "room", hasBody = true)
    Call<DeleteRoomResponse> deleteRoom(@Header ("Authorization") @NonNull String token, @Body @NonNull DeleteRoomBody body);
}

