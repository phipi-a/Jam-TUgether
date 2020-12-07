package de.pcps.jamtugether.api.services.room.bodies;

import androidx.annotation.NonNull;

public class CreateRoomBody {

    @NonNull
    private final String password;

    public CreateRoomBody(@NonNull String password) {
        this.password = password;
    }
}