package de.pcps.jamtugether.api.services.room.bodies;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.beat.Beat;

public class UpdateBeatBody {

    private final int roomID;

    @NonNull
    private final Beat beat;

    public UpdateBeatBody(int roomID, @NonNull Beat beat) {
        this.roomID = roomID;
        this.beat = beat;
    }
}
