package de.pcps.jamtugether.model;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class Composition {

    private final int roomID;

    @NonNull
    private final List<SingleSoundtrack> soundtracks;

    public Composition(int roomID, @NonNull List<SingleSoundtrack> soundtracks) {
        this.roomID = roomID;
        this.soundtracks = soundtracks;
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public List<SingleSoundtrack> getSoundtracks() {
        return soundtracks;
    }
}
