package de.pcps.jamtugether.models.soundtrack;

import androidx.annotation.NonNull;

import java.util.List;

public class Composition {

    private final int room;

    @NonNull
    private final List<Soundtrack> soundtracks;

    public Composition(int room, @NonNull List<Soundtrack> soundtracks) {
        this.room = room;
        this.soundtracks = soundtracks;
    }

    public int getRoom() {
        return room;
    }

    @NonNull
    public List<Soundtrack> getSoundtracks() {
        return soundtracks;
    }
}
