package de.pcps.jamtugether.model;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class Composition {

    private final int roomID;

    @NonNull
    private final Beat beat;

    @NonNull
    private final List<SingleSoundtrack> soundtracks;

    public Composition(int roomID, @NonNull Beat beat, @NonNull List<SingleSoundtrack> soundtracks) {
        this.roomID = roomID;
        this.beat = beat;
        this.soundtracks = soundtracks;
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public Beat getBeat() {
        return beat;
    }

    @NonNull
    public List<SingleSoundtrack> getSoundtracks() {
        return soundtracks;
    }
}
