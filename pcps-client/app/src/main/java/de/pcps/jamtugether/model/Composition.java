package de.pcps.jamtugether.model;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class Composition {

    private final int room;

    @NonNull
    private final List<SingleSoundtrack> singleSoundtracks;

    public Composition(int room, @NonNull List<SingleSoundtrack> singleSoundtracks) {
        this.room = room;
        this.singleSoundtracks = singleSoundtracks;
    }

    public int getRoom() {
        return room;
    }

    @NonNull
    public List<SingleSoundtrack> getSingleSoundtracks() {
        return singleSoundtracks;
    }
}
