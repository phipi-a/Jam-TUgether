package de.pcps.jamtugether.api.repositories;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.responses.room.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.room.DeleteRoomResponse;
import de.pcps.jamtugether.api.responses.room.JoinRoomResponse;
import de.pcps.jamtugether.api.services.room.RoomService;
import de.pcps.jamtugether.api.services.room.bodies.CreateRoomBody;
import de.pcps.jamtugether.api.services.room.bodies.DeleteRoomBody;
import de.pcps.jamtugether.api.services.room.bodies.JoinRoomBody;
import retrofit2.Call;

@Singleton
public class RoomRepository {

    @Inject
    RoomService roomService;

    @Inject
    public RoomRepository() { }

    public void createRoom(@NonNull String password, @NonNull JamCallback<CreateRoomResponse> callback) {
        CreateRoomBody body = new CreateRoomBody(password);
        Call<CreateRoomResponse> call = roomService.createRoom(body);
        call.enqueue(callback);
    }

    public void joinRoom(int roomID, @NonNull String password, @NonNull JamCallback<JoinRoomResponse> callback) {
        JoinRoomBody body = new JoinRoomBody(roomID, password);
        Call<JoinRoomResponse> call = roomService.joinRoom(body);
        call.enqueue(callback);
    }

    public void deleteRoom(int roomID, @NonNull String password, @NonNull String token, @NonNull JamCallback<DeleteRoomResponse> callback) {
        DeleteRoomBody body = new DeleteRoomBody(roomID, password);
        Call<DeleteRoomResponse> call = roomService.deleteRoom(String.format(Constants.BEARER_TOKEN_FORMAT, token), body);
        call.enqueue(callback);
    }
}
