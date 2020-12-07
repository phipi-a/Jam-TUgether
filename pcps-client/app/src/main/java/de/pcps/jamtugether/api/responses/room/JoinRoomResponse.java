package de.pcps.jamtugether.api.responses.room;

import androidx.annotation.NonNull;

public class JoinRoomResponse {

    private final int roomID;

    @NonNull
    private final String token;

    public JoinRoomResponse(int roomID, @NonNull String token) {
        this.roomID = roomID;
        this.token = token;
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public String getToken() {
        return token;
    }
}
