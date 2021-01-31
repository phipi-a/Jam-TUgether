package de.pcps.jamtugether.api.requests.room.responses;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.requests.base.SimpleResponse;

public class DeleteRoomResponse extends SimpleResponse {

    public DeleteRoomResponse(@NonNull String description) {
        super(description);
    }
}
