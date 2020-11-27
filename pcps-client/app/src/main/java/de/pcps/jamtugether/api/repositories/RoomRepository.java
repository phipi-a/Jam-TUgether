package de.pcps.jamtugether.api.repositories;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.api.responses.JoinRoomResponse;

// todo uncomment when room service is done
@Singleton
public class RoomRepository {

    //@Inject
    //RoomService roomService;

    @Inject
    public RoomRepository() {}

    public void createRoom(@NonNull String password, @NonNull BaseCallback<Integer> callback) {
        //Call<Integer> call = roomService.createRoom(password);
        //call.enqueue(callback);
    }

    public void joinRoom(int roomID, @NonNull String password, @NonNull BaseCallback<JoinRoomResponse> callback) {
        //Call<JoinRoomResponse> call = roomService.joinRoom(roomID, password);
        //call.enqueue(callback);
    }
}
