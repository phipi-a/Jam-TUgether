package de.pcps.jamtugether.model.music.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.instrument.base.Instrument;

// the client sends this object to the server
public class ServerSound extends Sound {

    private final int roomID;
    private final int userID;

    public ServerSound(int roomID, int userID, @NonNull Instrument instrument, int startTime, int endTime, int pitch) {
        super(instrument.getServerString(), startTime, endTime, pitch);
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
