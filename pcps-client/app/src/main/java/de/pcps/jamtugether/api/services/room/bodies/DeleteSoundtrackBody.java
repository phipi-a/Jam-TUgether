package de.pcps.jamtugether.api.services.room.bodies;

import androidx.annotation.NonNull;

public class DeleteSoundtrackBody {

    private final int userID;

    @NonNull
    private final String instrument;

    private final int number;

    public DeleteSoundtrackBody(int userID, @NonNull String instrument, int number) {
        this.userID = userID;
        this.instrument = instrument;
        this.number = number;
    }
}
