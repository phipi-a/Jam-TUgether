package de.pcps.jamtugether.models.music.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.models.instruments.Instrument;

// the client sends this object to the server
public class ServerSound extends Sound {

    private final int roomID;
    private final int userID;

    public ServerSound(int roomID, int userID, @NonNull Instrument instrument, int startTime, int pitch) {
        super(instrument.getServerString(), startTime, pitch);
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