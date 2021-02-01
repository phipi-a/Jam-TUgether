package de.pcps.jamtugether.api.requests.room.soundtrack;

import androidx.annotation.NonNull;

public class DeleteSoundtrackBody {

    private final int roomID;
    private final int userID;

    @NonNull
    private final String instrument;

    private final int number;

    public DeleteSoundtrackBody(int roomID, int userID, @NonNull String instrument, int number) {
        this.roomID = roomID;
        this.userID = userID;
        this.instrument = instrument;
        this.number = number;
    }
}
