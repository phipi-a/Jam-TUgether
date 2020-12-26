package de.pcps.jamtugether.model.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.instrument.base.Instrument;

// the client sends this object to the server
public class ServerSound extends Sound {

    private final int roomID;
    private final int userID;

    public ServerSound(int roomID, int userID, @NonNull Instrument instrument, int element, int startTime, int endTime, int pitch) {
        super(instrument.getServerString(), element, startTime, endTime, pitch);
        this.roomID = roomID;
        this.userID = userID;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getUserID() {
        return userID;
    }
}
