package de.pcps.jamtugether.api.responses.room;

import androidx.annotation.NonNull;

public class DeleteRoomResponse {

    @NonNull
    private final String description;

    public DeleteRoomResponse(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getDescription() {
        return description;
    }
}
