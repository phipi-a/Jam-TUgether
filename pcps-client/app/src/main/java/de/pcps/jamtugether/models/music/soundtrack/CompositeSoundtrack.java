package de.pcps.jamtugether.models.music.soundtrack;

import androidx.annotation.NonNull;

import java.util.List;

public class CompositeSoundtrack extends Soundtrack {

    @NonNull
    private final List<Soundtrack> soundtracks; // todo maybe make sure that every soundtrack is a single soundtrack

    public CompositeSoundtrack(@NonNull List<Soundtrack> soundtracks) {
        super();
        this.soundtracks = soundtracks;
    }

    @NonNull
    public List<Soundtrack> getSoundtracks() {
        return soundtracks;
    }

    public static CompositeSoundtrack from(@NonNull List<Soundtrack> soundtracks) {
        return new CompositeSoundtrack(soundtracks);
    }
}
