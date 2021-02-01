package de.pcps.jamtugether.api.requests.room.bodies;

import androidx.annotation.NonNull;

public class CreateRoomBody {

    @NonNull
    private final String password;

    public CreateRoomBody(@NonNull String password) {
        this.password = password;
    }
}