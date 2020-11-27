package de.pcps.jamtugether.api.services;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.JoinRoomResponse;
import retrofit2.Call;

// todo add actual endpoints with correct response classes
public interface RoomService {

    Call<CreateRoomResponse> createRoom(@NonNull String password);

    Call<JoinRoomResponse> joinRoom(int roomID, @NonNull String password);
}
