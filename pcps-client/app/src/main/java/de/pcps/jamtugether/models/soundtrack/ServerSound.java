package de.pcps.jamtugether.models.soundtrack;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.models.instrument.Instrument;

// the client sends this object to the server
public class ServerSound extends Sound {

    private final int room;
    private final int user;

    public ServerSound(int room, int user, @NonNull Instrument instrument, int startTime, int pitch) {
        super(instrument, startTime, pitch);
        this.room = room;
        this.user = user;
    }

    public int getRoom() {
        return room;
    }

    public int getUser() {
        return user;
    }
}
