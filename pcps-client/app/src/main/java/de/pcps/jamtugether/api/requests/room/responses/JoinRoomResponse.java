package de.pcps.jamtugether.api.requests.room.responses;

import androidx.annotation.NonNull;

public class JoinRoomResponse {

    private final int roomID;
    private final int userID;

    @NonNull
    private final String token;

    public JoinRoomResponse(int roomID, int userID, @NonNull String token) {
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
