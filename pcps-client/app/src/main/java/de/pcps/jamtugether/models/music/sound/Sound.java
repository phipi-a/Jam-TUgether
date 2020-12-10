package de.pcps.jamtugether.models.music.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.models.instruments.Instrument;
import de.pcps.jamtugether.models.instruments.Instruments;

// the client receives this object from the server
public class Sound {

    @NonNull
    private final String instrument;

    private final int startTime;
    private final int pitch;

    public Sound(@NonNull String instrument, int startTime, int pitch) {
        this.instrument = instrument;
        this.startTime = startTime;
        this.pitch = pitch;
    }

    @NonNull
    public Instrument getInstrument() {
        return Instruments.fromServer(instrument);
    }

    public int getStartTime() {
        return startTime;
    }

    public int getPitch() {
        return pitch;
    }
}
