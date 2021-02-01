package de.pcps.jamtugether.api.requests.room.beat;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.Beat;

public class UpdateBeatBody {

    private final int roomID;

    @NonNull
    private final Beat beat;

    public UpdateBeatBody(int roomID, @NonNull Beat beat) {
        this.roomID = roomID;
        this.beat = beat;
    }
}
