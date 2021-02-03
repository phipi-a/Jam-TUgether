package de.pcps.jamtugether.api.requests.room.create;

import androidx.annotation.NonNull;

public class CreateRoomBody {

    @NonNull
    private final String password;

    public CreateRoomBody(@NonNull String password) {
        this.password = password;
    }
}