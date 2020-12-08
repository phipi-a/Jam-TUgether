package de.pcps.jamtugether.models;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.models.instruments.Instrument;

// the client receives this object from the server
public class Sound {

    @NonNull
    private final String instrument;

    private final int startTime;
    private final int pitch;

    public Sound(@NonNull Instrument instrument, int startTime, int pitch) { // todo maybe convert instrument to string through JsonAdapter
        this.instrument = instrument.getServerString();
        this.startTime = startTime;
        this.pitch = pitch;
    }

    @NonNull
    public String getInstrument() {
        return instrument;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getPitch() {
        return pitch;
    }
}
