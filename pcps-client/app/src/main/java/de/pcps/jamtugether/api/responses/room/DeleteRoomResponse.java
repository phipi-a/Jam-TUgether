package de.pcps.jamtugether.api.responses.room;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.base.SimpleResponse;

public class DeleteRoomResponse extends SimpleResponse {

    public DeleteRoomResponse(@NonNull String description) {
        super(description);
    }
}
