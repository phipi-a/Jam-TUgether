package de.pcps.jamtugether.api.services.room.bodies;

import androidx.annotation.NonNull;

public class DeleteTrackBody {

    private final int roomID;
    private final int userID;

    @NonNull
    private final String instrument;

    @NonNull
    private final int[] soundSequence = new int[3];

    public DeleteTrackBody(int roomID, @NonNull int userID, @NonNull String instrument, @NonNull int startTime, @NonNull int endTime, @NonNull int pitch) {
        this.roomID = roomID;
        this.userID = userID;
        this.instrument = instrument;
        this.soundSequence[0] = startTime;
        this.soundSequence[1] = endTime;
        this.soundSequence[2] = pitch;
    }
}
