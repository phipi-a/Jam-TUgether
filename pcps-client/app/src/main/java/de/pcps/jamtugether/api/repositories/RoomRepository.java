package de.pcps.jamtugether.api.repositories;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.api.responses.room.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.room.JoinRoomResponse;
import de.pcps.jamtugether.api.services.room.RoomService;
import de.pcps.jamtugether.api.services.room.bodies.CreateRoomBody;
import de.pcps.jamtugether.api.services.room.bodies.JoinRoomBody;
import retrofit2.Call;

@Singleton
public class RoomRepository {

    @Inject
    RoomService roomService;

    @Inject
    public RoomRepository() { }

    public void createRoom(@NonNull String password, @NonNull BaseCallback<CreateRoomResponse> callback) {
        CreateRoomBody body = new CreateRoomBody(password);
        Call<CreateRoomResponse> call = roomService.createRoom(body);
        call.enqueue(callback);
    }

    public void joinRoom(int roomID, @NonNull String password, @NonNull BaseCallback<JoinRoomResponse> callback) {
        JoinRoomBody body = new JoinRoomBody(roomID, password);
        Call<JoinRoomResponse> call = roomService.joinRoom(body);
        call.enqueue(callback);
    }
}
