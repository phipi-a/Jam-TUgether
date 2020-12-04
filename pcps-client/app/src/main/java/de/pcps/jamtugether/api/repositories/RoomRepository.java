package de.pcps.jamtugether.api.repositories;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.api.responses.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.JoinRoomResponse;
import de.pcps.jamtugether.api.services.RoomService;
import retrofit2.Call;

// todo uncomment when room service is done
@Singleton
public class RoomRepository {

    @Inject
    RoomService roomService;

    @Inject
    public RoomRepository() { }

    public void createRoom(@NonNull String password, @NonNull BaseCallback<CreateRoomResponse> callback) {
        RoomService.CreateRoomBody body = new RoomService.CreateRoomBody(password);
        Call<CreateRoomResponse> call = roomService.createRoom(body);
        call.enqueue(callback);
    }

    public void joinRoom(int roomID, @NonNull String password, @NonNull BaseCallback<JoinRoomResponse> callback) {
        RoomService.JoinRoomBody body = new RoomService.JoinRoomBody(roomID, password);
        Call<JoinRoomResponse> call = roomService.joinRoom(body);
        call.enqueue(callback);
    }
}
