package de.pcps.jamtugether.api.requests.room.join;

import androidx.annotation.NonNull;

public class JoinRoomBody {

    private final int roomID;

    @NonNull
    private final String password;

    public JoinRoomBody(int roomID, @NonNull String password) {
        this.password = password;
        this.roomID = roomID;
    }
}