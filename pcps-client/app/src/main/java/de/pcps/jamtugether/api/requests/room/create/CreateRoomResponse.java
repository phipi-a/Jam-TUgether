package de.pcps.jamtugether.api.requests.room.create;

import androidx.annotation.NonNull;

public class CreateRoomResponse {

    private final int roomID;
    private final int userID;

    @NonNull
    private final String token;

    public CreateRoomResponse(int roomID, int userID, @NonNull String token) {
        this.roomID = roomID;
        this.userID = userID;
        this.token = token;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getUserID() {
        return userID;
    }

    @NonNull
    public String getToken() {
        return token;
    }
}
