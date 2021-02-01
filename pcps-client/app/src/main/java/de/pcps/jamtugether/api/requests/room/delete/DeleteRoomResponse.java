package de.pcps.jamtugether.api.requests.room.delete;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.requests.SimpleResponse;

public class DeleteRoomResponse extends SimpleResponse {

    public DeleteRoomResponse(@NonNull String description) {
        super(description);
    }
}
