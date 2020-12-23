package de.pcps.jamtugether.model.music.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;

// the client receives this object from the server
public class Sound {

    public static final int MIN_PITCH = 0;
    public static final int MAX_PITCH = 100;
    public static final int PITCH_RANGE = MAX_PITCH - MIN_PITCH;

    @NonNull
    private final String instrument;

    private final int element;

    private final int startTime;
    private final int endTime;
    private final int pitch;

    public Sound(@NonNull String instrument, int element, int startTime, int endTime, int pitch) {
        this.instrument = instrument;
        this.element = element;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pitch = pitch;
    }

    @NonNull
    public String getInstrument() {
        return instrument;
    }

    public int getElement() {
        return element;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getPitch() {
        return pitch;
    }

    public int getLength() {
        return endTime - startTime;
    }
}
