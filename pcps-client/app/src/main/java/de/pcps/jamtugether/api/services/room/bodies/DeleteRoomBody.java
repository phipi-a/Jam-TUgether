package de.pcps.jamtugether.api.services.room.bodies;

import androidx.annotation.NonNull;

public class DeleteRoomBody {

    private final int roomID;

    @NonNull
    private final String password;

    public DeleteRoomBody(int roomID, @NonNull String password) {
        this.roomID = roomID;
        this.password = password;
    }
}
