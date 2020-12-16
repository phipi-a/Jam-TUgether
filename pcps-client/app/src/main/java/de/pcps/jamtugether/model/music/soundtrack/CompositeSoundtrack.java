package de.pcps.jamtugether.model.music.soundtrack;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;

public class CompositeSoundtrack extends Soundtrack {

    @NonNull
    private final List<SingleSoundtrack> soundtracks;

    public CompositeSoundtrack(@NonNull List<SingleSoundtrack> soundtracks) {
        super();
        this.soundtracks = soundtracks;
    }

    @NonNull
    public List<SingleSoundtrack> getSoundtracks() {
        return soundtracks;
    }

    @NonNull
    public static CompositeSoundtrack from(@NonNull List<SingleSoundtrack> soundtracks) {
        return new CompositeSoundtrack(soundtracks);
    }
}